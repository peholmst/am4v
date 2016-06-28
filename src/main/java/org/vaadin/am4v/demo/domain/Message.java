package org.vaadin.am4v.demo.domain;

import java.time.ZonedDateTime;
import java.util.*;

public class Message extends Entity {

    private String from;
    private String subject;
    private Set<String> to;
    private Set<String> cc;
    private ZonedDateTime date;
    private String body;

    public Message(UUID uuid, String from, String subject, Collection<String> to, Collection<String> cc,
        ZonedDateTime date, String body) {
        super(uuid);
        this.from = Objects.requireNonNull(from);
        this.subject = Objects.requireNonNull(subject);
        this.to = Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(to)));
        this.cc = Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(cc)));
        this.date = Objects.requireNonNull(date);
        this.body = Objects.requireNonNull(body);
    }

    public Message(String from, String subject, Collection<String> to, Collection<String> cc, ZonedDateTime date,
        String body) {
        this.from = Objects.requireNonNull(from);
        this.subject = Objects.requireNonNull(subject);
        this.to = Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(to)));
        this.cc = Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(cc)));
        this.date = Objects.requireNonNull(date);
        this.body = Objects.requireNonNull(body);
    }

    public Message(Message original) {
        super(original);
        this.from = original.from;
        this.subject = original.subject;
        this.to = Collections.unmodifiableSet(new HashSet<>(original.to));
        this.cc = Collections.unmodifiableSet(new HashSet<>(original.cc));
        this.date = original.date;
        this.body = original.body;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public Set<String> getTo() {
        return to;
    }

    public Set<String> getCc() {
        return cc;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }
}
