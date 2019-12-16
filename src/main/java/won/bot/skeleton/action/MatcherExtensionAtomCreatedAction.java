package won.bot.skeleton.action;

import org.apache.commons.collections.CollectionUtils;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import won.bot.framework.eventbot.EventListenerContext;
import won.bot.framework.eventbot.action.BaseEventBotAction;
import won.bot.framework.eventbot.event.Event;
import won.bot.framework.eventbot.event.impl.command.connect.ConnectCommandEvent;
import won.bot.framework.eventbot.listener.EventListener;
import won.bot.framework.extensions.matcher.MatcherExtensionAtomCreatedEvent;
import won.bot.framework.extensions.serviceatom.ServiceAtomContext;
import won.bot.skeleton.context.SkeletonBotContextWrapper;
import won.bot.skeleton.location.City;
import won.bot.skeleton.location.GDS;
import won.protocol.model.Coordinate;
import won.protocol.util.DefaultAtomModelWrapper;
import won.protocol.util.linkeddata.WonLinkedDataUtils;
import won.protocol.vocabulary.WXCHAT;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MatcherExtensionAtomCreatedAction extends BaseEventBotAction {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private List<String> validTags = Arrays.asList("location information", "locationinformation", "location-information",
            "position information", "positioninformation", "position-information", "pi", "li");
    private static final GDS testGDS = new GDS();

    public MatcherExtensionAtomCreatedAction(EventListenerContext eventListenerContext) {
        super(eventListenerContext);

    }

    @Override
    protected void doRun(Event event, EventListener executingListener) throws Exception {
        EventListenerContext ctx = getEventListenerContext();
        if (!(event instanceof MatcherExtensionAtomCreatedEvent) || !(getEventListenerContext().getBotContextWrapper() instanceof SkeletonBotContextWrapper)) {
            logger.error("MatcherExtensionAtomCreatedAction can only handle MatcherExtensionAtomCreatedEvent and only works with SkeletonBotContextWrapper");
            return;
        }
        SkeletonBotContextWrapper botContextWrapper = (SkeletonBotContextWrapper) ctx.getBotContextWrapper();
        MatcherExtensionAtomCreatedEvent atomCreatedEvent = (MatcherExtensionAtomCreatedEvent) event;

        DefaultAtomModelWrapper defaultAtomModelWrapper = new DefaultAtomModelWrapper(atomCreatedEvent.getAtomData());

        if (!CollectionUtils.containsAny(defaultAtomModelWrapper.getAllTags(), validTags)) {
            return;
        }
        System.out.println("Atom for me: " + atomCreatedEvent.getAtomData().getUnionModel());

        Coordinate coordinate = null;
        for (Resource node : defaultAtomModelWrapper.getSeeksNodes()) {
            coordinate = defaultAtomModelWrapper.getLocationCoordinate(node);
        }
        if (coordinate == null) return;

        ServiceAtomContext c = (ServiceAtomContext) ctx.getBotContextWrapper();
        URI myAtomURI = c.getServiceAtomUri();
        System.out.println("myAtomURI: " + myAtomURI);

        Collection<URI> collection = WonLinkedDataUtils.getSocketsOfType(myAtomURI, new URI(WXCHAT.ChatSocketString), ctx.getLinkedDataSource());
        System.out.println("getLinkedDataSources " + collection.toString());
        URI myAtomSocketURI = (URI) collection.toArray()[0];
        System.out.println("myAtomSocketURI: " + myAtomSocketURI);

        Collection<String> socketUris = defaultAtomModelWrapper.getSocketUris();
        String uri = "";
        for (String uris : socketUris) {
            if (WonLinkedDataUtils.isCompatibleSockets(ctx.getLinkedDataSource(), new URI(uris), myAtomSocketURI)) {
                uri = uris;
            }
        }

        System.out.println("uri: " + uri);
        String message = constructMessage(coordinate);

        URI targetURI = new URI(uri);
        if (!uri.isEmpty()) {
            System.out.println("socket is present");
            botContextWrapper.addConnectedSocket(myAtomSocketURI, targetURI);
            ConnectCommandEvent connectCommandEvent = new ConnectCommandEvent(myAtomSocketURI, targetURI, message);
            System.out.println("connectCommand: " + connectCommandEvent);
            ctx.getEventBus().publish(connectCommandEvent);
            // botContextWrapper.removeConnectedSocket(myAtomSocketURI,socketType.get());
        }
    }

    private String constructMessage(Coordinate coordinate) {
        List<City> cityByLngLat = testGDS.getCityByLngLat(coordinate.getLongitude(), coordinate.getLatitude());
        return cityByLngLat.stream().map(City::toString).collect(Collectors.joining(", "));
    }
}
