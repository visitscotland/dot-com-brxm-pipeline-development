import { shallowMount } from '@vue/test-utils';

import VsIknowPartnerItem from '../IknowPartnerItem';

const headingSlot = 'Heading Slot';
const textSlot = 'Text Slot';
const ctaSlot = 'CTA Slot';

const factoryShallowMount = () => shallowMount(VsIknowPartnerItem, {
    slots: {
        iknowHeading: headingSlot,
        iknowText: textSlot,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsIknowParterItem', () => {
    it('should render a component with `vs-iknow-partner-item` data-test attribute', () => {
        expect(wrapper.attributes('data-test')).toContain('vs-iknow-partner-item');
    });

    it('should render a cta wrapper if cta is inserted in the `iknow-cta` slot', () => {
        const modifiedWrapper = shallowMount(VsIknowPartnerItem, {
            slots: {
                iknowCta: ctaSlot,
            },
        });

        const ctaWrapper = modifiedWrapper.find('.vs-iknow-partner-item__cta');

        expect(ctaWrapper.exists()).toBe(true);
    });

    it('should *NOT* render a cta wrapper if no cta is inserted in the `iknow-cta` slot', () => {
        const ctaWrapper = wrapper.find('.vs-iknow-partner-item__cta');
        expect(ctaWrapper.exists()).not.toBe(true);
    });

    describe(':slots', () => {
        it('renders content inserted in the `iknow-heading` slot', () => {
            expect(wrapper.text()).toContain(headingSlot);
        });

        it('renders content inserted in the `iknow-text` slot', () => {
            expect(wrapper.text()).toContain(textSlot);
        });

        it('renders content inserted in the `iknow-cta` slot', () => {
            const modifiedWrapper = shallowMount(VsIknowPartnerItem, {
                slots: {
                    iknowCta: ctaSlot,
                },
            });

            expect(modifiedWrapper.text()).toContain(ctaSlot);
        });
    });
});
