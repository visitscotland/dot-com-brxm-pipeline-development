import { shallowMount } from '@vue/test-utils';

import VsSummaryBoxDistanceListItem from '../SummaryBoxDistanceListItem';

const factoryShallowMount = (propsData) => shallowMount(VsSummaryBoxDistanceListItem, {
    propsData: {
        miles: '10',
        milesLabel: 'miles',
        kilometres: '20',
        kilometresLabel: 'kilometres',
        ...propsData,
    },
});

describe('VsSummaryBoxDistanceListItem', () => {
    it('should render a component with the data-test attribute `vs-summary-box-distance-list-item`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.attributes('data-test')).toBe('vs-summary-box-distance-list-item');
    });

    describe(':props', () => {
        it('it should accept and render a `miles` property', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.text()).toContain('10');
        });

        it('if `showMiles` is false, it should accept and render a `kilometres` property', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.text()).toContain('20');
        });

        it('should accept an distanceLabel property', () => {
            const wrapper = factoryShallowMount({
                distanceLabel: 'Entfernung',
            });
            expect(wrapper.html()).toContain('Entfernung');
        });

        it('should accept an milesLabel property', () => {
            const label = 'meilen';
            const wrapper = factoryShallowMount({
                milesLabel: label,
            });
            expect(wrapper.html()).toContain(label);
        });

        it('should accept an kilometresLabel property', () => {
            const label = 'kilomètres';
            const wrapper = factoryShallowMount({
                kilometresLabel: label,
            });
            expect(wrapper.html()).toContain(label);
        });
    });
});
