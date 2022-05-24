import { mount } from '@vue/test-utils';
import VsCannedSearchSubHeading from '../CannedSearchSubHeading';

const subHeading = 'Callander, Loch Lomond';

const factoryShallowMount = (propsData) => mount(VsCannedSearchSubHeading, {
    propsData: {
        subHeading,
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsCannedSearchSubHeading', () => {
    describe(':props', () => {
        it('should render the content of the `subHeading` property', () => {
            expect(wrapper.html()).toContain(subHeading);
        });

        it('should render a subheading with the class `vs-canned-search-sub-heading--lines-1` if a lineLimit of 1 is provided', async() => {
            await wrapper.setProps({
                lineLimit: 1,
            });

            expect(wrapper.classes()).toContain('vs-canned-search-sub-heading--lines-1');
        });

        it('should render a subheading with the class `vs-canned-search-sub-heading--lines-2` if a lineLimit of 2 is provided', async() => {
            await wrapper.setProps({
                lineLimit: 2,
            });

            expect(wrapper.classes()).toContain('vs-canned-search-sub-heading--lines-2');
        });
    });
});
