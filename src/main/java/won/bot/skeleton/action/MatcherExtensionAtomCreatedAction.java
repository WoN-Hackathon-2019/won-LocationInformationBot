package won.bot.skeleton.action;

import org.apache.commons.collections.CollectionUtils;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import won.bot.framework.eventbot.EventListenerContext;
import won.bot.framework.eventbot.action.BaseEventBotAction;
import won.bot.framework.eventbot.event.Event;
import won.bot.framework.eventbot.event.impl.command.connect.ConnectCommandEvent;
import won.bot.framework.eventbot.event.impl.command.connect.ConnectCommandResultEvent;
import won.bot.framework.eventbot.event.impl.command.connect.ConnectCommandSuccessEvent;
import won.bot.framework.eventbot.filter.impl.CommandResultFilter;
import won.bot.framework.eventbot.listener.EventListener;
import won.bot.framework.eventbot.listener.impl.ActionOnFirstEventListener;
import won.bot.framework.extensions.matcher.MatcherExtensionAtomCreatedEvent;
import won.bot.framework.extensions.serviceatom.ServiceAtomContext;
import won.bot.skeleton.context.SkeletonBotContextWrapper;
import won.bot.skeleton.location.City;
import won.bot.skeleton.location.LocationData;
import won.protocol.model.Coordinate;
import won.protocol.util.DefaultAtomModelWrapper;
import won.protocol.util.linkeddata.LinkedDataSource;
import won.protocol.util.linkeddata.WonLinkedDataUtils;
import won.protocol.vocabulary.SCHEMA;
import won.protocol.vocabulary.WXCHAT;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MatcherExtensionAtomCreatedAction extends BaseEventBotAction {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private List<String> validTags = Arrays.asList("location information", "locationinformation", "location-information",
            "position information", "positioninformation", "position-information", "pi", "li");
    private static final LocationData testGDS = new LocationData();

    //GlobalChatinator
    private static String globalChatinator = "TranslateBot";
    private static URI getGlobalChatinatorURI;

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

        ServiceAtomContext c = (ServiceAtomContext) ctx.getBotContextWrapper();
        URI myAtomURI = c.getServiceAtomUri();

        Collection<URI> collection = WonLinkedDataUtils.getSocketsOfType(myAtomURI, new URI(WXCHAT.ChatSocketString), ctx.getLinkedDataSource());
        URI myAtomSocketURI = (URI) collection.toArray()[0];

        try {
            if (defaultAtomModelWrapper.getContentPropertyStringValue(SCHEMA.NAME).equals(globalChatinator)) {
                System.out.println("Bot found");
                System.out.println(defaultAtomModelWrapper.getSomeName());
                initiateTranslator(botContextWrapper, defaultAtomModelWrapper, ctx.getLinkedDataSource(), myAtomSocketURI);
                return;
            }
        } catch (Exception ignored) {
        }

        if (!CollectionUtils.containsAny(defaultAtomModelWrapper.getAllTags(), validTags)) {
            return;
        }
        System.out.println("Atom for me: " + atomCreatedEvent.getAtomData().getUnionModel());
        System.out.println("myAtomURI: " + myAtomURI);
        System.out.println("getLinkedDataSources " + collection.toString());
        System.out.println("myAtomSocketURI: " + myAtomSocketURI);

        Coordinate coordinate = null;
        for (Resource node : defaultAtomModelWrapper.getSeeksNodes()) {
            coordinate = defaultAtomModelWrapper.getLocationCoordinate(node);
        }
        if (coordinate == null) return;

        String message = constructMessage(coordinate);

        String uri = getSocketURIStringOfAtom(defaultAtomModelWrapper, ctx.getLinkedDataSource(), myAtomSocketURI);
        URI targetURI = new URI(uri);
        System.out.println("uri: " + targetURI);
        if (!uri.isEmpty()) {
            System.out.println("socket is present");
            botContextWrapper.addConnectedSocket(myAtomSocketURI, targetURI);
            ConnectCommandEvent connectCommandEvent = new ConnectCommandEvent(myAtomSocketURI, targetURI, message);
            System.out.println("connectCommand: " + connectCommandEvent);
            ctx.getEventBus().subscribe(ConnectCommandSuccessEvent.class, new ActionOnFirstEventListener(ctx,
                    new CommandResultFilter(connectCommandEvent), new BaseEventBotAction(ctx) {
                @Override
                protected void doRun(Event event, EventListener eventListener) {
                    ConnectCommandResultEvent connectionMessageCommandResultEvent = (ConnectCommandResultEvent) event;
                    if (connectionMessageCommandResultEvent.isSuccess()) {
                        botContextWrapper.removeConnectedSocket(myAtomSocketURI, targetURI);
                    }
                }
            }));
        }
    }

    private String constructMessage(Coordinate coordinate) {
        List<City> cityByLngLat = testGDS.getCityByLngLat(coordinate.getLongitude(), coordinate.getLatitude());
        return cityByLngLat.stream().map(City::toString).collect(Collectors.joining(", "));
    }

    private String getSocketURIStringOfAtom(DefaultAtomModelWrapper defaultAtomModelWrapper, LinkedDataSource linkedDataSource, URI myAtomSocketURI) throws URISyntaxException {
        Collection<String> socketUris = defaultAtomModelWrapper.getSocketUris();
        String uri = "";
        for (String uris : socketUris) {
            if (WonLinkedDataUtils.isCompatibleSockets(linkedDataSource, new URI(uris), myAtomSocketURI)) {
                uri = uris;
            }
        }
        return uri;
    }

    private void initiateTranslator(SkeletonBotContextWrapper botContextWrapper,
                                    DefaultAtomModelWrapper defaultAtomModelWrapper, LinkedDataSource linkedDataSource,
                                    URI myAtomSocketURI) throws URISyntaxException {
        getGlobalChatinatorURI = new URI(getSocketURIStringOfAtom(defaultAtomModelWrapper, linkedDataSource, myAtomSocketURI));
        System.out.println("uri: " + getGlobalChatinatorURI);
        System.out.println("socket is present");
        botContextWrapper.addConnectedSocket(myAtomSocketURI, getGlobalChatinatorURI);
    }
}
