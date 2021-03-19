const { vueHelper } = require('@cypress/helpers/index.js').default;
const VsSkipTo = require('../SkipTo.vue').default;

const skipToLabel = 'Skip to thing';

const targetId1 = 'target-1';
const targetId2 = 'target-2';

const childContent = `
  <vs-skip-to tabindex="0" :target="target" @activated="$emit('activated')">${skipToLabel}</vs-skip-to>
  <div tabindex="1">Thing to skip</div>
  <div tabindex="2" ref="skipToTarget" id="${targetId1}">Thing to skip to</div>
  <div tabindex="3" id="${targetId2}">Second thing to skip to</div>
`;

describe('Skip To component using ref target', () => {
    const mergeVue = {
        data() {
            return {
                target: null,
            };
        },
        components: {
            VsSkipTo,
        },
        mounted() {
            this.target = this.$refs.skipToTarget;
        },
    };

    vueHelper.init('div', null, {
        childContent,
        mergeVue,
    });

    it('should render a button containing the text and an icon', () => {
        cy.get('button.vs-skip-to')
            .should('have.class', 'sr-only sr-only-focusable')
            .should('have.attr', 'tabindex', '0')
            .get('*')
            .should('be.hidden')
            .contains('svg');

        cy.get('button.vs-skip-to').contains(skipToLabel);
    });

    it('should show when it has focus', () => {
        cy.get('button.vs-skip-to')
            .get('*')
            .should('be.hidden');

        cy.get('button.vs-skip-to')
            .focus()
            .get('*')
            .should('be.visible');
    });

    it('should give focus to the target element when clicked then emit the activated event', () => {
        const spy = cy.spy();

        Cypress.vue.$on('activated', spy);

        cy.get('button.vs-skip-to')
            .focus()
            .click()
            .then(() => {
                cy.get(`#${targetId1}`).should('have.focus');
                // eslint-disable-next-line no-unused-expressions
                expect(spy).to.be.called;
            });
    });
});

describe('Skip To component using HTML Element target', () => {
    const mergeVue = {
        data() {
            return {
                target: null,
            };
        },
        components: {
            VsSkipTo,
        },
        mounted() {
            cy.get(`#${targetId2}`).then((el) => {
                this.target = el;
            });
        },
    };

    vueHelper.init('div', null, {
        childContent,
        mergeVue,
    });

    it('should give focus to the target element when clicked then emit the activated event', () => {
        const spy = cy.spy();

        Cypress.vue.$on('activated', spy);

        cy.get('button.vs-skip-to')
            .focus()
            .get('*')
            .should('be.visible');

        cy.get('button.vs-skip-to')
            .focus()
            .click()
            .then(() => {
                cy.get(`#${targetId2}`).should('have.focus');
                // eslint-disable-next-line no-unused-expressions
                expect(spy).to.be.called;
            });
    });
});
