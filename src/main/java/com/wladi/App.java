package com.wladi;

import java.net.URL;
import java.util.List;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class App extends ListenerAdapter {

    public static final String TOKEN = "MTA3NjI1NTA3NjE2OTA0MDAxMg.GJFqjw.Kwjxc7BjkqDkuTTroj0sI90xHeRCOKyCDajdE8";
    public static final String RSS = "https://www.tabnews.com.br/recentes/rss";

    public static void main(String[] args) {
        JDA client = JDABuilder.createLight(TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
            .build();
        client.addEventListener(new App());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String messageContent = event.getMessage().getContentRaw();
    
        if (messageContent.equalsIgnoreCase("/sobre")) {
            event.getChannel()
            .sendMessage("Existimos porque somos um time! \n\nA Horizon é uma empresa que desenvolve tecnologias para inovar o mercado. Buscamos entregar mais do que serviços ou produtos, somos aficionados por entregar soluções de grande impacto positivo! \n\nCada integrante do nosso time incorpora e traz consigo o espírito da inovação e do empreendedorismo para dentro e fora da empresa. Quer seja através do desenvolvimento de um software ou de um hardware, entendemos que nosso maior resultado virá através da satisfação de cada um dos nossos clientes.")
            .queue();
        }

        if (messageContent.equalsIgnoreCase("/produtos")) {
            event.getChannel()
            .sendMessage("Nossos cases de sucesso. 🤩 \nMAS - _https://www.portalmas.com.br/_ \nHealthPlus - _https://www.portalhealthplus.com.br/_")
            .queue();
        }
    
    
        if (messageContent.startsWith("/pergunta")) {
            String question = messageContent.substring("/pergunta".length()).trim();
            if (!question.isEmpty()) {
                String answer = getAnswer(question);
                event.getChannel().sendMessage(answer).queue();
            } else {
                event.getChannel().sendMessage("Por favor, digite uma pergunta.").queue();
            }
        }

        if (messageContent.equalsIgnoreCase("/noticias")) {
            event.getChannel().sendMessage("Aqui estão as últimas noticias e publicações do TabNews. :incoming_envelope: \n").queue();
            for (SyndEntry entry : rssContent().subList(0, Math.min(3, rssContent().size()))) {
                String menssage = "";
                menssage = menssage + "||◍||\n\n **" + entry.getTitle() + "** \n _" + entry.getDescription().getValue() + "_ \n _" + entry.getLink() + "_";
                event.getChannel()
                .sendMessage(menssage)
                .queue();
                String thumbnail = "https://www.tabnews.com.br/api/v1/contents/${link}"; 
                event.getChannel().sendMessage(thumbnail.replace("${link}", entry.getLink().substring(27)) + "/thumbnail \n\n").queue();
            }
        }

    }

    private String getAnswer(String question) {
        return "A resposta para \"" + question + "\" é 42.";
    }

    public List<SyndEntry> rssContent() {
        try {
            URL url = new URL(RSS);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            List<SyndEntry> entries = feed.getEntries();

            return entries;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } 
    }

}