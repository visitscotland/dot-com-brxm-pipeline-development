import { shallowMount } from '@vue/test-utils';
import VsCannedSearchTourDeparts from '../CannedSearchTourDeparts';

const label = 'Departs';

const origins = [
    {
        name: 'Edinburgh',
    },
    {
        name: 'Glasgow',
    },
    {
        name: 'Kilmartin',
    },
];

const factoryShallowMount = () => shallowMount(VsCannedSearchTourDeparts, {
    propsData: {
        label,
        origins,
    },
});

describe('VsCannedSearchTourDeparts', () => {
    describe(':props', () => {
        it('should render the `label` property', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.html()).toContain(label);
        });

        it('should render each item within the `origins` property', () => {
            const wrapper = factoryShallowMount();

            for (let x = 0; x < origins.length; x++) {
                expect(wrapper.html()).toContain(origins[x].name);
            }
        });

        it('should properly render the origins as the `transformedOrigins` computed prop', () => {
            const wrapper = factoryShallowMount();
            const expectedOutput = 'Edinburgh, Glasgow, Kilmartin';

            expect(wrapper.html()).toContain(expectedOutput);
        });
    });
});
