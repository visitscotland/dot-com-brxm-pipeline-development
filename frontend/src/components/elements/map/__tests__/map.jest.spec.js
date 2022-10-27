import { shallowMount } from '@vue/test-utils';

import VsMap from '../Map';

const factoryShallowMount = () => shallowMount(VsMap, {
    methods: {
        initialiseMapComponent: jest.fn(),
    },
    propsData: {
        mapId: 'vs-map',
        isVisible: true,
        places: '',
    },
});

beforeEach(() => {
    window.URL.createObjectURL = jest.fn();
});

describe('VsMap', () => {
    it('should render a map component with the data-test attribute `vs-map`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.find('[data-test="vs-map"]').exists()).toBe(true);
    });

    describe(':props', () => {
        it('should use the `mapId` prop for an id attribute div element', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.find('.vs-map__map').attributes('id')).toBe('vs-map');
        });
    });
});
