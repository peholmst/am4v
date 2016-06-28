package org.vaadin.am4v.demo.domain;

import de.svenjacobs.loremipsum.LoremIpsum;

import java.time.ZonedDateTime;
import java.util.*;

public class MessageService {

    private static final MessageService INSTANCE = new MessageService();

    private Map<Folder, List<Message>> folderToMessagesMap = new HashMap<>();

    public static MessageService getInstance() {
        return INSTANCE;
    }

    private MessageService() {
        folderToMessagesMap.put(FolderService.getInstance().inbox, new ArrayList<>());
        folderToMessagesMap.put(FolderService.getInstance().junk, new ArrayList<>());
        folderToMessagesMap.put(FolderService.getInstance().trash, new ArrayList<>());
        for (int i = 0; i < 100; i++) {
            folderToMessagesMap.get(FolderService.getInstance().inbox).add(createMessage());
            if (i % 2 == 0) {
                folderToMessagesMap.get(FolderService.getInstance().junk).add(createMessage());
            }
            if (i % 3 == 0) {
                folderToMessagesMap.get(FolderService.getInstance().trash).add(createMessage());
            }
        }
    }

    private final Random rnd = new Random();

    private Message createMessage() {
        LoremIpsum loremIpsum = new LoremIpsum();

        String from = Names.getRandomName();
        Set<String> to = new HashSet<>();
        for (int i = 0; i < rnd.nextInt(5) + 1; ++i) {
            to.add(Names.getRandomName());
        }
        Set<String> cc = new HashSet<>();
        if (rnd.nextInt(10) == 0) {
            for (int i = 0; i < rnd.nextInt(5); ++i) {
                cc.add(Names.getRandomName());
            }
        }

        ZonedDateTime date = ZonedDateTime.now().minusMinutes(rnd.nextInt(1000000));

        return new Message(from, loremIpsum.getWords(5), to, cc, date, loremIpsum.getParagraphs(rnd.nextInt(10)));
    }

    public List<Message> getMessagesInFolder(Folder folder) {
        return new ArrayList<>(folderToMessagesMap.getOrDefault(folder, Collections.emptyList()));
    }
}
