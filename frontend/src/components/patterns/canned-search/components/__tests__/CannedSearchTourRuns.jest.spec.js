import { shallowMount } from '@vue/test-utils';
import VsCannedSearchTourRuns from '../CannedSearchTourRuns';

const label = 'Runs';
const startDay = 'June';
const endDay = 'September';

const factoryShallowMount = () => shallowMount(VsCannedSearchTourRuns, {
    propsData: {
        label,
        startDay,
        endDay,
    },
});

describe('VsCannedSearchTourRuns', () => {
    describe(':props', () => {
        it('should render the `label` property', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.html()).toContain(label);
        });

        it('should render the `startDay` property', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.html()).toContain(startDay);
        });

        it('should render the `endDay` property', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.html()).toContain(endDay);
        });

        it('should properly render the period as the `formattedPeriod` computed prop if both `startDay` and `endDay` are present', () => {
            const wrapper = factoryShallowMount();
            const expectedOutput = 'Runs: June - September';

            expect(wrapper.html()).toContain(expectedOutput);
        });

        it('should properly render the period as the `formattedPeriod` computed prop if `endDay` is not present', async() => {
            const wrapper = factoryShallowMount();

            wrapper.setProps({
                startDay: 'All Year Round',
                endDay: null,
            });

            const expectedOutput = 'Runs: All Year Round';

            await wrapper.vm.$nextTick();

            expect(wrapper.html()).toContain(expectedOutput);
        });
    });
});
