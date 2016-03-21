package com.ran.filesystem.descriptor;

import java.nio.file.Path;

public class AuthorDecisionDescriptor extends EntityDescriptor {

    private static final String ROOT_ELEMENT = "authorDecision",
                                TITLE = "title",
                                COMPILATOR_NAME = "compilator";
    
    public static AuthorDecisionDescriptor getEmptyAuthorDecisionDescriptor(Path path) {
        AuthorDecisionDescriptor descriptor = new AuthorDecisionDescriptor(path);
        descriptor.persist();
        return descriptor;
    }

    public static AuthorDecisionDescriptor getExistingAuthorDecisionDescriptor(Path path) {
        AuthorDecisionDescriptor descriptor = new AuthorDecisionDescriptor(path);
        descriptor.load();
        return descriptor;
    }

    private AuthorDecisionDescriptor(Path path) {
        super(path, ROOT_ELEMENT);
        setProperty(TITLE, "");
        setProperty(COMPILATOR_NAME, "");
    }
    
    public String getTitle() {
        return getProperty(TITLE);
    }
    
    public void setTitle(String title) {
        setProperty(TITLE, title);
    }
    
    public String getCompilatorName() {
        return getProperty(COMPILATOR_NAME);
    }
    
    public void setCompilatorName(String compilatorName) {
        setProperty(COMPILATOR_NAME, compilatorName);
    }
    
}