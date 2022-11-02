import { shallowMount } from '@vue/test-utils';
import VsMainMapWrapperListItem from '../MainMapWrapperListItem';

const factoryShallowMount = () => shallowMount(VsMainMapWrapperListItem, {
    slots: {
        default: 'Button text',
    },
    propsData: {
        itemData: {
            category: {
                id: 'cities',
                label: 'Cities',
            },
            title: 'Glasgow',
            id: 'glasgow',
            image: 'https://cimg.visitscotland.com/cms-images/attractions/outlander/claire-standing-stones-craigh-na-dun-outlander?size=sm',
        },
    },
    computed: {
        isActive() {
            return true;
        },
        highlightedPlace() {
            return 'highlighted-id';
        },
    },
});

describe('VsMainMapWrapperListItem', () => {
    it('should render a component with the data-test attribute `vs-main-map-wrapper-list-item`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.attributes('data-test')).toBe('vs-main-map-wrapper-list-item');
    });

    describe(':props', () => {
        it('should render the button text from the `itemData` prop', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.text()).toBe('Glasgow');
        });
    });

    describe(':methods', () => {
        it('should emit `show-item-detail` with a value of the `itemData` prop id when the button is clicked', async() => {
            const wrapper = factoryShallowMount();
            const mockMethod = jest.spyOn(wrapper.vm, 'showItemDetail');
            await wrapper.trigger('click');

            expect(mockMethod).toHaveBeenCalledWith('glasgow');
        });
    });
});
