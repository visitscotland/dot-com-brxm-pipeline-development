import { shallowMount } from '@vue/test-utils';
import VsCannedSearch from '../CannedSearch';

const apiUrl = 'http://172.28.81.65:8089/data/search/productsearch?areaproxdist=10&loc=Scotland&locplace=&locprox=1&prodtypes=acco&locale=';
const slotContent = 'These are some buttons';
const creditSlotContent = 'This is some credit info';

const factoryShallowMount = () => shallowMount(VsCannedSearch, {
    propsData: {
        linkUrl: apiUrl,
    },
    slots: {
        vsCannedSearchButtons: slotContent,
        vsCannedSearchCredit: creditSlotContent,
    },
});

describe('VsCannedSearch', () => {
    it('should render a component with the data-test attribute `vs-canned-search`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.find('[data-test="vs-canned-search"]').exists()).toBe(true);
    });

    describe(':slots', () => {
        it('should render the content of the `vsCannedSearchButtons` slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.html()).toContain(slotContent);
        });

        it('should render the content of the `vsCannedSearchCredit` slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.html()).toContain(creditSlotContent);
        });
    });
});
