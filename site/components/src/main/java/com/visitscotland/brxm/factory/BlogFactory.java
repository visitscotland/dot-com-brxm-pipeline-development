package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.Blog;
import com.visitscotland.brxm.hippobeans.Profile;
import com.visitscotland.brxm.model.FlatBlog;
import com.visitscotland.brxm.services.ResourceBundleService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;


@Component
public class BlogFactory {

    private final ResourceBundleService bundle;

    private static final String BLOG_LABELS = "essentials.global";
    public BlogFactory( ResourceBundleService bundle){
        this.bundle = bundle;
    }

    //TODO refactor and review/redo in phase 1, temporary solution to unblock content team while CPP project
    public FlatBlog getBlog(Blog doc, Locale locale, Collection<String> errorMessages){
        FlatBlog blog = new FlatBlog();
        if (doc.getAuthor() instanceof Profile) {
            blog.setAuthorName(doc.getAuthor().getName());
        }
        blog.setPublishDate(doc.getPublishDate().getTime());
        if (doc.getReadingTime() > 0) {
            //TODO consider moving the labels to modules in the future depending on the next phases
            String readTime = bundle.getResourceBundle(BLOG_LABELS,"read-time.plural", locale);
            if (doc.getReadingTime() < 2) {
                readTime = bundle.getResourceBundle(BLOG_LABELS,"read-time.singular", locale);
            }
            blog.setReadingTime(bundle.getResourceBundle(BLOG_LABELS,"read-time", locale) + "" +doc.getReadingTime().toString() + " " + readTime);
        } else {
            errorMessages.add("The Read Time for the blog should be greater than 0 minutes.");
        }

        return blog;
    }

}
