import { shallowMount } from '@vue/test-utils';

import VsQuote from '../Quote';

const factoryShallowMount = (slotsData) => shallowMount(VsQuote, {
    slots: {
        ...slotsData,
    },
});

describe('VsIknowParterItem', () => {
    it('should render a component with `vs-quote` data-test attribute', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toContain('vs-quote');
    });

    describe(':slots', () => {
        it('renders content inserted in the `quoteImage` slot', () => {
            const imageContent = 'Quote image';
            const wrapper = factoryShallowMount({
                quoteImage: imageContent,
            });

            expect(wrapper.text()).toContain(imageContent);
        });

        it('renders content inserted in the `quoteContent` slot', () => {
            const mainQuoteContent = 'Quote test';
            const wrapper = factoryShallowMount({
                quoteContent: mainQuoteContent,
            });

            expect(wrapper.text()).toContain(mainQuoteContent);
        });

        it('renders content inserted in the `quoteAuthorName` slot', () => {
            const authorContent = 'Quote author name';
            const wrapper = factoryShallowMount({
                quoteAuthorName: authorContent,
            });

            expect(wrapper.text()).toContain(authorContent);
        });

        it('renders content inserted in the `quoteAuthorTitle` slot', () => {
            const titleContent = 'Quote author title';
            const wrapper = factoryShallowMount({
                quoteAuthorTitle: titleContent,
            });

            expect(wrapper.text()).toContain(titleContent);
        });

        it('renders content inserted in the `quoteLink` slot', () => {
            const linkContent = 'Quote link';
            const wrapper = factoryShallowMount({
                quoteLink: linkContent,
            });

            expect(wrapper.text()).toContain(linkContent);
        });
    });
});
