import { mount } from '@vue/test-utils';
import VsCannedSearchSummaryBox from '../CannedSearchSummaryBox';

const summaryLeftSlotContent = 'This is a price';
const summaryTopSlotContent = 'This is a date';
const summaryCentreSlotContent = 'This is a duration';

const linkHref = 'https://google.com';
const linkType = 'external';
const linkLabel = 'Visit Website';

const factoryMount = () => mount(VsCannedSearchSummaryBox, {
    propsData: {
        linkHref,
        linkType,
        linkLabel,
    },
    slots: {
        vsCannedSearchSummaryLeft: summaryLeftSlotContent,
        vsCannedSearchSummaryTop: summaryTopSlotContent,
        vsCannedSearchSummaryCentre: summaryCentreSlotContent,
    },
    provide: () => ({
        slideVisible: () => true,
    }),
});

let wrapper;
beforeEach(() => {
    wrapper = factoryMount();
});

describe('VsCannedSearchSummaryBox', () => {
    describe(':slots', () => {
        it('should render the content of the `vsCannedSearchSummaryLeft` slot', () => {
            expect(wrapper.html()).toContain(summaryLeftSlotContent);
        });

        it('should render the content of the `vsCannedSearchSummaryTop` slot', () => {
            expect(wrapper.html()).toContain(summaryTopSlotContent);
        });

        it('should render the content of the `vsCannedSearchSummaryCentre` slot', () => {
            expect(wrapper.html()).toContain(summaryCentreSlotContent);
        });
    });

    describe(':props', () => {
        it('should render a `vs-link` with the `href` provided in `linkHref`', () => {
            const link = wrapper.find('[data-test="vs-canned-search-summary-box__link"]');

            expect(link.props().href).toBe(linkHref);
        });

        it('should render a `vs-link` with the `type` provided in `linkType`', () => {
            const link = wrapper.find('[data-test="vs-canned-search-summary-box__link"]');

            expect(link.props().type).toBe(linkType);
        });

        it('should render the content of the `linkLabel` property', () => {
            expect(wrapper.html()).toContain(linkLabel);
        });
    });
});
