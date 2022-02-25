package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.hippobeans.ListicleItem;
import com.visitscotland.brxm.hippobeans.Stop;
import com.visitscotland.brxm.translation.SessionFactory;
import com.visitscotland.utils.Contract;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Optional;

/**
 * jcr:Name = visitscotland:location-validator

 */
public class LocationValidator implements Validator<Node> {

    private SessionFactory sessionFactory;

    public LocationValidator() {
        this.sessionFactory = new SessionFactory();
    }

    LocationValidator(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public Optional<Violation> validate(final ValidationContext context, final Node document) {
        try {
            Node node = null;
            if (document.hasNode(Stop.PRODUCTS)){
                node = sessionFactory.getJcrSession().getNodeByIdentifier(document.getNode(Stop.PRODUCTS).getIdentifier());
                }else if (document.hasNode(ListicleItem.PRODUCT)){
                    node = sessionFactory.getJcrSession().getNodeByIdentifier(document.getNode(ListicleItem.PRODUCT).getIdentifier());
                }
            if (node != null) {
                //if the product is not dms, the subtitle/location should be provided
                if (!node.hasProperty("visitscotland:product")) {
                    if (Contract.isEmpty(document.getProperty("visitscotland:subtitle").getString())) {
                        return Optional.of(context.createViolation());
                    }
                }
            }
        } catch (RepositoryException e) {
            return Optional.of(context.createViolation());
        }

        return Optional.empty();
    }
}

