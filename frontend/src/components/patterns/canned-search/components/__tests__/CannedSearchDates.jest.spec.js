import { mount } from '@vue/test-utils';
import VsCannedSearchDates from '../CannedSearchDates';

const intro = 'Dates';
const period = {
    startDay: '2022-10-12',
    endDay: '2022-10-14',
};

const factoryShallowMount = (propsData) => mount(VsCannedSearchDates, {
    propsData: {
        intro,
        period,
        ...propsData,
    },
});

describe('VsCannedSearchDates', () => {
    describe(':props', () => {
        it('should render the content of the `intro` property', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.html()).toContain(intro);
        });

        it('should render a date range if `startDay` and `endDay` are present', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.html()).toContain(`${period.startDay} - ${period.endDay}`);
        });
    });
});
