package com.visitscotland.brxm.validator;

import com.visitscotland.brxm.hippobeans.Coordinates;
import org.onehippo.cms.services.validation.api.ValidationContext;
import org.onehippo.cms.services.validation.api.ValidationContextException;
import org.onehippo.cms.services.validation.api.Validator;
import org.onehippo.cms.services.validation.api.Violation;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Optional;

/**
 * jcr:Name = visitscotland:scotland-coordinates-validator
 *
 * @author jose.calcines
 */
public class CoordinatesValidator implements Validator<Node> {

    static final String NORTH_EXTREME = "extreme.north";
    static final String SOUTH_EXTREME = "extreme.south";
    static final String EAST_EXTREME = "extreme.east";
    static final String WEST_EXTREME = "extreme.west";

    private double north;
    private double south;
    private double east;
    private double west;

    public CoordinatesValidator(final Node config) {
        String nodePath = null;
        try {
            nodePath = config.getPath();

            north = config.getProperty(NORTH_EXTREME).getDouble();
            south = config.getProperty(SOUTH_EXTREME).getDouble();
            east = config.getProperty(EAST_EXTREME).getDouble();
            west = config.getProperty(WEST_EXTREME).getDouble();
            if (north < south || east < west) {
                throw new ValidationContextException("Coordinates configuration is wrong. Verify the node " + nodePath);
            }
        } catch (NullPointerException e) {
            throw new ValidationContextException("Coordinates configuration is wrong. Verify the node " + nodePath);
        } catch (RepositoryException e) {
            if (nodePath == null) {
                throw new ValidationContextException("Unexpected Error when loading CoordinatesValidator class", e);
            } else {
                throw new ValidationContextException("Cannot read required properties for the CoordinatesValidator. Verify the node " + nodePath, e);
            }
        }
    }

    public Optional<Violation> validate(final ValidationContext context, final Node document) {
        try {
            if (document.hasProperty(Coordinates.LATITUDE) && document.hasProperty(Coordinates.LONGITUDE)) {
                double lat = document.getProperty(Coordinates.LATITUDE).getDouble();
                double lon = document.getProperty(Coordinates.LONGITUDE).getDouble();
                if (north < lat || lat < south || east < lon || lon < west) {
                    return Optional.of(context.createViolation());
                }
            }
        } catch (RepositoryException e) {
            return Optional.of(context.createViolation());
        }

        return Optional.empty();
    }
}
