package com.visitscotland.brxm.utils;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MessageCache {

    private static final Logger logger = LoggerFactory.getLogger(MessageCache.class);

    private Properties properties;

    public MessageCache(Properties properties){
        this.properties = properties;
    }

    public boolean canLog(String message, Object... args){
        Message issue = new Message(message, args);

        int index = messages.indexOf(issue);

        if (index < 0) {
            cleanUpIfNeeded();
            issue.log();
            messages.add(issue);
        } else {
            issue = messages.get(index);
            if (issue.getLastOccurrence().before(new Date(new Date().getTime() + properties.getContentCacheRetention()))){
                //TODO: verify that the messages go together
                logger.info("The next content message has been logged {} time(s) in the last {}s", issue.getCount(), 1000);
                issue.reset();
            } else {
                return false;
            }
        }
        return true;
    }

    ArrayList<Message> messages = new ArrayList<>();

    private void cleanUpIfNeeded(){
        if (messages.size() > properties.getContentCacheMaxElements()){

            long cutPoint = new Date().getTime() - properties.getContentCacheRetention();
            List<Message> removeElements = new ArrayList<>();

            for (Message message: messages){
                if (message.lastOccurrence.getTime() < cutPoint){
                    removeElements.add(message);
                }
            }

            //TODO this shouldn't be a warning
            logger.warn("Content Cache Clean up execurted because more than {} were recorded. The size of the cache was reduced by {}%",
                    properties.getContentCacheRetention(), new Double(100 * removeElements.size()/properties.getContentCacheRetention()).intValue());

            synchronized (MessageCache.class){
                Collections.sort(messages);
                for (int i=0; i <properties.getContentCacheMaxElements()/2;i++){
                    messages.remove(0);
                }
            }
        }

        //TODO: Create a clean up method and scheduled process to execute the clean up
        //Helpful link: https://www.baeldung.com/spring-scheduled-tasks
    }

    private class Message implements Serializable, Comparable<Message> {
        private final String message;
        private final Object[] arguments;
        private Date firstOccurrence;
        private Date lastOccurrence;
        private int count = 0;

        private Message(String message, Object... args){
            this.message = message;
            this.arguments = args;
        }

        public String getMessage() {
            return message;
        }

        public Object[] getArguments() {
            return arguments;
        }

        public Date getFirstOccurrence() {
            return firstOccurrence;
        }

        public Date getLastOccurrence() {
            return lastOccurrence;
        }

        public int getCount() {
            return count;
        }

        @Override
        public int compareTo(@NotNull MessageCache.Message message) {
            return lastOccurrence.compareTo(message.lastOccurrence);
        }

        void log(){
            count++;
            lastOccurrence = new Date();
            if (firstOccurrence == null ){
                firstOccurrence = lastOccurrence;
            }
        }

        void reset(){
            count = 1;
            lastOccurrence = new Date();
            firstOccurrence = lastOccurrence;
        }

        //Autogenerated by IntelliJ
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Message message1 = (Message) o;
            if (!message.equals(message1.message)) return false;
            return Arrays.equals(arguments, message1.arguments);
        }

        //Autogenerated by IntelliJ
        @Override
        public int hashCode() {
            int result = message.hashCode();
            result = 31 * result + Arrays.hashCode(arguments);
            return result;
        }
    }
}
