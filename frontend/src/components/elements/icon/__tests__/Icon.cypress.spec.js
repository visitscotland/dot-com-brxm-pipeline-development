const { vueHelper, getToken } = require('@cypress/helpers/index.js').default;
const { each } = require('lodash');
const VsIcon = require('../Icon.vue').default;

describe('Icon component', () => {
    const props = {
        size: 'md',
        name: 'favourite',
        reverse: null,
        variant: null,
    };
    const allSizes = {
        'x-small': 'xs',
        small: 'sm',
        medium: 'md',
        large: 'lg',
        'x-large': 'xl',
    };

    const primaryColour = getToken('color_theme_primary');
    const secondaryColour = getToken('color_theme_secondary');
    const whiteColour = getToken('color_white');

    vueHelper.init('vs-icon', VsIcon, {
        props,
    });

    it('renders the default favourite icon with the appropriate class', () => {
        cy.get('svg')
            .should('have.class', 'icon')
            .should('have.class', 'icon-md')
            .should('have.class', 'icon-favourite');
    });

    each(allSizes, (size, name) => {
        it(`renders the favourite icon at ${name} size`, () => {
            const sizeValue = getToken(`icon_size_${size}`);
            const sizeInt = parseInt(sizeValue, 10);
            const padding = sizeInt / 4;
            const dimension = sizeInt + padding * 2;

            vueHelper.setProp('size', size);

            cy.get('svg')
                .should('have.class', `icon-${size}`)
                .should('have.css', 'height', `${dimension}px`)
                .should('have.css', 'width', `${dimension}px`)
                .should('have.css', 'padding', `${padding}px`);
        });
    });

    it('renders the applies appropriate class of icon', () => {
        vueHelper.setProp('name', 'search');

        cy.get('svg').should('have.class', 'icon');
    });

    it('renders the applies appropriate class based on the name', () => {
        vueHelper.setProp('name', 'search');

        cy.get('svg')
            .should('have.class', 'icon')
            .should('have.class', 'icon-search');
    });

    it('renders the applies appropriate class based on the default size', () => {
        vueHelper.setProp('name', 'favourite');

        cy.get('svg').should('have.class', 'icon-md');
    });

    it('renders the favourite icon with a primary theme', () => {
        vueHelper.setProp('variant', 'primary');

        cy.get('svg')
            .should('have.class', 'icon')
            .should('have.class', 'icon-md')
            .should('have.class', 'icon-favourite')
            .should('have.css', 'fill', primaryColour);
    });

    it('renders the search icon in reverse', () => {
        const blackColour = getToken('color_black');

        vueHelper.setProp('name', 'search');
        vueHelper.setProp('reverse', true);

        cy.get('svg')
            .should('have.class', 'icon')
            .should('have.class', 'icon-md')
            .should('have.class', 'icon-search')
            .should('have.class', 'icon-reverse')
            .should('have.css', 'fill', whiteColour)
            .should('have.css', 'background-color', blackColour);
    });

    it('renders the secondary variant of the favourite icon in reverse', () => {
        vueHelper.setProp('reverse', true);
        vueHelper.setProp('variant', 'secondary');

        cy.get('svg')
            .should('have.class', 'icon')
            .should('have.class', 'icon-md')
            .should('have.class', 'icon-favourite')
            .should('have.class', 'icon-reverse')
            .should('have.css', 'fill', whiteColour)
            .should('have.css', 'background-color', secondaryColour);
    });
});
