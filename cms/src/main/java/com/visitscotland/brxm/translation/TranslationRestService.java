package com.visitscotland.brxm.translation;

import com.visitscotland.brxm.translation.plugin.JcrDocument;
import com.visitscotland.brxm.translation.plugin.JcrDocumentFactory;
import org.hippoecm.repository.api.WorkflowException;
import org.hippoecm.repository.translation.HippoTranslationNodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

import javax.jcr.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

@RestController
@RequestScope
public class TranslationRestService {
    private static final Logger log = LoggerFactory.getLogger(TranslationRestService.class);
    private SessionFactory sessionFactory;
    private JcrDocumentFactory jcrDocumentFactory;
    private TranslationService translationService;

    @Autowired
    public TranslationRestService(SessionFactory sessionFactory,
                                  JcrDocumentFactory jcrDocumentFactory,
                                  TranslationService translationService) {
        this.sessionFactory = sessionFactory;
        this.jcrDocumentFactory = jcrDocumentFactory;
        this.translationService = translationService;
    }

    @GetMapping(value = "/vs-service/node/{handleId}/translation/diff", produces = "application/json")
    public String getNodeDifference(@PathVariable String handleId) {
        try {
            Node handleNode = sessionFactory.getJcrSession().getNodeByIdentifier(handleId);
            JcrDocument jcrDocument = jcrDocumentFactory.createFromNode(handleNode);

            Node unpublishedNode = jcrDocument.getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
            Property localeProperty = unpublishedNode.getProperty(HippoTranslationNodeType.LOCALE);
            if (Locale.ENGLISH.getLanguage().equals(localeProperty.getString())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to get difference for English document");
            }

            String diff = translationService.getDocumentDifference(handleId);
            if ( null == diff ) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No difference found for document");
            }
            return diff;
        } catch(ItemNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No node found with given node identifier");
        } catch(IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch(RepositoryException | IOException ex) {
            if (ex.getCause() instanceof IllegalArgumentException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            } else {
                throw new ResponseStatusException((HttpStatus.INTERNAL_SERVER_ERROR));
            }
        }
    }

    @PostMapping(value = "/vs-service/node/{handleId}/translation/flag", produces = "application/json")
    public void setTranslationFlag(@PathVariable String handleId, @RequestBody String body) {
        // Needs to be an english document to set the translation on
        try {
            Node handleNode = sessionFactory.getJcrSession().getNodeByIdentifier(handleId);
            JcrDocument jcrDocument = jcrDocumentFactory.createFromNode(handleNode);

            TranslationService.TranslationContent content = new TranslationService.TranslationContent();
            content.setContent(body);
            List<JcrDocument> documentsBlockingEdit = translationService.setTranslationContent(jcrDocument, content);
            if (!documentsBlockingEdit.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Unable to lock documents for edit");
            }
        } catch(IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No foreign language documents");
        } catch(WorkflowException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Unable to lock documents for edit");
        } catch(ItemNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No node found with given node identifier");
        } catch(IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch(RepositoryException | RemoteException ex) {
            if (ex.getCause() instanceof IllegalArgumentException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            } else {
                log.error("Failed to send document for translation", ex);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
            }
        }
    }

    @DeleteMapping(value = "/vs-service/node/{handleId}/translation/flag", produces = "application/json")
    public void deleteTranslationFlag(@PathVariable String handleId) {
        try {
            Node handleNode = sessionFactory.getJcrSession().getNodeByIdentifier(handleId);
            JcrDocument jcrDocument = jcrDocumentFactory.createFromNode(handleNode);

            translationService.clearTranslationFlag(jcrDocument);

        } catch(WorkflowException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Unable to lock document for edit");
        } catch(ItemNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No node found with given node identifier");
        } catch(IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch(RepositoryException | RemoteException ex) {
            if (ex.getCause() instanceof IllegalArgumentException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            } else {
                throw new ResponseStatusException((HttpStatus.INTERNAL_SERVER_ERROR));
            }
        }
    }
}
