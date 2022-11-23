import { shallowMount } from '@vue/test-utils';

import VsMapMarker from '../MapMarker';
import markerExample from './data/markerData.json';

const mockMethod = jest.fn();
const factoryShallowMount = () => shallowMount(VsMapMarker, {
    propsData: {
        feature: markerExample,
        mapId: 'vs-map',
    },
    computed: {
        isActive() {
            return true;
        },
        highlightedPlace() {
            return 'highlighted-id';
        },
    },
    methods: {
        handleClick: mockMethod,
    },
});

describe('VsMapMarker', () => {
    it('should render a map component with the data-test attribute `vs-map-marker`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-map-marker');
    });

    describe(':methods', () => {
        it('should fire the `handleClick` method when clicked', async() => {
            const wrapper = factoryShallowMount();

            await wrapper.trigger('click');
            expect(mockMethod).toHaveBeenCalled();
        });
    });

    describe(':props', () => {
        it('should populate the child SVG component marker attribute with the `type` defined in the `feature` prop', () => {
            const wrapper = factoryShallowMount();

            const svg = wrapper.find('vssvg-stub');
            expect(svg.attributes('path')).toBe('marker-cities');
        });
    });
});
