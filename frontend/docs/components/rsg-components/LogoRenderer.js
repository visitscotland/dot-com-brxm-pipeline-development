import React from 'react';
import PropTypes from 'prop-types';
import Styled from 'rsg-components/Styled';
import logo from '@images/ds-logo-png.png';

const styles = () => ({
	image: {
		width: '230px',
        cursor: 'pointer',
	},
    homeLink: {
        display: 'block',
    },
});

export function LogoRenderer({ classes }) {
    return (
        <a className={classes.homeLink} href="/">
            <img className={classes.image} src={logo} alt="VisitScotland Logo" key="vs-logo-img"/>
        </a>
	);
}

LogoRenderer.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default Styled(styles)(LogoRenderer);
