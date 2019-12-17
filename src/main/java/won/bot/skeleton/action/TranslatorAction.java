package won.bot.skeleton.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import won.bot.framework.eventbot.EventListenerContext;
import won.bot.framework.eventbot.action.BaseEventBotAction;
import won.bot.framework.eventbot.event.Event;
import won.bot.framework.eventbot.event.MessageEvent;
import won.bot.framework.eventbot.event.impl.command.connectionmessage.ConnectionMessageCommandEvent;
import won.bot.framework.eventbot.event.impl.wonmessage.MessageFromOtherAtomEvent;
import won.bot.framework.eventbot.listener.EventListener;
import won.bot.skeleton.location.TranslatorCommand;
import won.protocol.message.WonMessage;
import won.protocol.model.Connection;
import won.protocol.util.WonRdfUtils;

import java.net.URI;

public class TranslatorAction extends BaseEventBotAction {

    protected TranslatorAction(EventListenerContext eventListenerContext) {
        super(eventListenerContext);
    }

    @Override
    protected void doRun(Event event, EventListener eventListener) throws Exception {
        EventListenerContext ctx = getEventListenerContext();
        if (event instanceof MessageFromOtherAtomEvent
                ) { //&& ctx.getBotContextWrapper() instanceof JokeBotContextWrapper
            Connection con = ((MessageFromOtherAtomEvent) event).getCon();
            URI transalateAtomUri = con.getAtomURI();
            String message = "";
            try {
                WonMessage msg = ((MessageEvent) event).getWonMessage();
                message = extractTextMessageFromWonMessage(msg);
            } catch (Exception te) {
            }

            String responseMessage = TranslatorCommand.getParsedMessageFromResponse(message);

            try {
                Model messageModel = WonRdfUtils.MessageUtils.textMessage(responseMessage);
                getEventListenerContext().getEventBus().publish(new ConnectionMessageCommandEvent(con, messageModel));
            } catch (Exception te) {
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
