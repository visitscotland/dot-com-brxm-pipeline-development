import React from 'react';
import PropTypes from 'prop-types';
import Version from 'rsg-components/Version';
import Markdown from 'rsg-components/Markdown';
import Logo from 'rsg-components/Logo';
import Styled from 'rsg-components/Styled';

const xsmall = '@media (max-width: 600px)';

const styles = ({ font, maxWidth, space, color }) => ({
    root: {
		minHeight: '100vh',
		backgroundColor: color.baseBackground,
	},
    header: {
        color: '#41b883',
        backgroundColor: 'white',
        padding: [[space[2], 0]],
        marginBottom: space[4],
        boxShadow: '0 2px 6px 0 rgb(0 0 0 / 16%)',
    },
    headerLink: {},
    nav: {},
    logoWrapper: {
        padding: space[2],
        borderBottom: [[1, color.border, 'solid']],
    },
    navBar: {
        display: 'flex',
    },
    sidebar: {
        flex: '0 0 auto',
    },
    sections: {
        flex: '0 0 auto',
    },
    contentWrapper: {
		maxWidth,
		margin: [[0, 'auto']],
		display: 'block',
	},
    content:{
        display: 'flex',
        flexWrap: 'wrap',
    },
    mainMenuList: {
        margin: '0',
    },
    mainMenuListItem: {
        listStyleType: 'none',
        display: 'inline-block',
    },
    components: {
        overflow: 'auto', // To prevent the pane from growing out of the screen
    },
    footer: {},
    footerText:{},
    mainContent:{},
});

export function StyleGuideRenderer({
    classes,
    title,
    children,
    version,
    homepageUrl,
    toc,
    hasSidebar,
}) {
    return (
        <div className={classes.root}>
            <div className={classes.mainContent}>
                <header className={classes.header}>
                    <div className={classes.contentWrapper}>
                        <div className={classes.navBar}>
                            <div className={classes.logoWrapper}>
                                <Logo>{title}</Logo>
                            </div>
                            <nav className={classes.nav} aria-label="sub-menu">
                                <ul className={classes.mainMenuList}>
                                    <li className={classes.mainMenuListItem}>
                                        <a
                                            className={classes.headerLink}
                                            href="https://vue-styleguidist.github.io"
                                        >
                                            Docs
                                        </a>
                                    </li>
                                    <li className={classes.mainMenuListItem}>
                                        <a
                                            className={classes.headerLink}
                                            href="https://github.com/vue-styleguidist/vue-styleguidist"
                                        >
                                            GitHub
                                        </a>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </header>
                <main className={classes.contentWrapper}>
                    <div className={classes.content}>
                        {hasSidebar && (
                            <div className={classes.sidebar} data-testid="sidebar">
                                {toc}
                                {/* {version && <Version>{version}</Version>} */}
                            </div>
                        )}
                        <div className={classes.sections} data-testid="sections">
                            {children}
                        </div>
                    </div>
                </main>
            </div>
            <footer className={classes.footer}>
                <p className={classes.footerText}>&copy; 2021 VisitScotland. All rights reserved.</p>
            </footer>
        </div>
    );
}

StyleGuideRenderer.propTypes = {
    classes: PropTypes.object.isRequired,
    title: PropTypes.string.isRequired,
    version: PropTypes.string,
    homepageUrl: PropTypes.string.isRequired,
    children: PropTypes.node.isRequired,
    toc: PropTypes.node.isRequired,
    hasSidebar: PropTypes.bool,
};

export default Styled(styles)(StyleGuideRenderer);
