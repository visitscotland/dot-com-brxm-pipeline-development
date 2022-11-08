import { shallowMount } from '@vue/test-utils';
import VsProductSearch from '../ProductSearch';

const factoryShallowMount = (slotData) => shallowMount(VsProductSearch, {
    propsData: {
        configArr: [
            {
                subSearchType: 'acco',
            },
        ],
        noJsMessage: 'This is the no JS message',
    },
    ...slotData,
});

describe('VsProductSearch', () => {
    describe(':slots', () => {
        it('should render heading slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    vsModuleHeading: 'This is the Product Search heading',
                },
            });

            expect(wrapper.html()).toContain('This is the Product Search heading');
        });

        it('should render intro slot content', () => {
            const wrapper = factoryShallowMount({
                slots: {
                    vsModuleIntro: 'This is the Product Search intro',
                },
            });

            expect(wrapper.html()).toContain('This is the Product Search intro');
        });
    });

    describe(':props', () => {
        it('should pass props to the child component', () => {
            const wrapper = factoryShallowMount();

            const embedStub = wrapper.find('vsProductSearchEmbed-stub');
            expect(embedStub.attributes('config')).toBe('[object Object]');
        });

        it('should render `noJsMessage` prop content', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.html()).toContain('This is the no JS message');
        });
    });
});
