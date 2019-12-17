package won.bot.skeleton.action;

import org.apache.commons.lang3.StringUtils;
import won.bot.framework.eventbot.EventListenerContext;
import won.bot.framework.eventbot.action.BaseEventBotAction;
import won.bot.framework.eventbot.event.Event;
import won.bot.framework.eventbot.event.MessageEvent;
import won.bot.framework.eventbot.event.impl.command.connect.ConnectCommandEvent;
import won.bot.framework.eventbot.event.impl.wonmessage.MessageFromOtherAtomEvent;
import won.bot.framework.eventbot.listener.EventListener;
import won.bot.framework.extensions.serviceatom.ServiceAtomContext;
import won.bot.skeleton.location.TranslatorCommand;
import won.protocol.message.WonMessage;
import won.protocol.model.Connection;
import won.protocol.util.WonRdfUtils;
import won.protocol.util.linkeddata.WonLinkedDataUtils;
import won.protocol.vocabulary.WXCHAT;

import java.net.URI;
import java.util.Collection;

public class TranslatorAction extends BaseEventBotAction {

    public TranslatorAction(EventListenerContext eventListenerContext) {
        super(eventListenerContext);
    }

    @Override
    protected void doRun(Event event, EventListener eventListener) throws Exception {
        EventListenerContext ctx = getEventListenerContext();
        System.out.println("triggered");
        if (event instanceof MessageFromOtherAtomEvent
                ) {
            Connection con = ((MessageFromOtherAtomEvent) event).getCon();
            URI transalateAtomUri = con.getAtomURI();
            String message = "";
            try {
                WonMessage msg = ((MessageEvent) event).getWonMessage();
                message = extractTextMessageFromWonMessage(msg);
            } catch (Exception te) {
            }

            System.out.println(message);
            if (message == null || message.equals("")) {
                message = "No Response";
            }
            System.out.println("Response in chat: " + message);

            ServiceAtomContext c = (ServiceAtomContext) ctx.getBotContextWrapper();
            URI myAtomURI = c.getServiceAtomUri();

            Collection<URI> collection = WonLinkedDataUtils.getSocketsOfType(myAtomURI, new URI(WXCHAT.ChatSocketString), ctx.getLinkedDataSource());
            URI myAtomSocketURI = (URI) collection.toArray()[0];

            String uri = TranslatorCommand.getUri(message);
            if(uri==null||uri.isEmpty()){
                return;
            }
            System.out.println("uri: " + uri);
            URI tmpUri = new URI(uri);
            if (!uri.isEmpty()) {
                System.out.println("socket is present");
                ConnectCommandEvent connectCommandEvent = new ConnectCommandEvent(myAtomSocketURI, tmpUri,
                        TranslatorCommand.getParsedMessageFromResponse(message));
                System.out.println("send: " + message);
                System.out.println("connectCommand: " + connectCommandEvent);
                ctx.getEventBus().publish(connectCommandEvent);
            }
        }
    }

    private String extractTextMessageFromWonMessage(WonMessage wonMessage) {
        if (wonMessage == null)
            return null;
        String message = WonRdfUtils.MessageUtils.getTextMessage(wonMessage);
        return StringUtils.trim(message);
    }
}
