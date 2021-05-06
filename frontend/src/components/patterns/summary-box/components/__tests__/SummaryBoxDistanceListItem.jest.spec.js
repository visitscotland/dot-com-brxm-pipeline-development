import { shallowMount, mount } from '@vue/test-utils';

import VsSummaryBoxDistanceListItem from '../SummaryBoxDistanceListItem';

const spyToggleShowMiles = jest.spyOn(VsSummaryBoxDistanceListItem.methods, 'toggleShowMiles');

const factoryShallowMount = (propsData) => shallowMount(VsSummaryBoxDistanceListItem, {
    propsData: {
        miles: '10',
        milesLabel: 'miles',
        kilometres: '20',
        kilometresLabel: 'kilometres',
        ...propsData,
    },
});

const factoryMount = (propsData) => mount(VsSummaryBoxDistanceListItem, {
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
        it('if `showMiles` is true, it should accept and render a `miles` property', () => {
            const wrapper = factoryShallowMount({
                showMiles: true,
            });

            expect(wrapper.text()).toContain('10');
        });

        it('if `showMiles` is false, it should accept and render a `kilometres` property', () => {
            const wrapper = factoryShallowMount({
                showMiles: false,
            });

            expect(wrapper.text()).toContain('20');
        });

        it('if `showMiles` is true, it should render a `display_miles` div with the miles value', () => {
            const wrapper = factoryShallowMount({
                showMiles: true,
            });

            const milesDiv = wrapper.find('#display_miles');
            expect(milesDiv.exists()).toBe(true);
        });

        it('if `showMiles` is false, it should render a `display_kilometres` div with the km value', () => {
            const wrapper = factoryShallowMount({
                showMiles: false,
            });

            const kmDiv = wrapper.find('#display_kilometres');
            expect(kmDiv.exists()).toBe(true);
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

        it('should accept an milesAbbr property', () => {
            const wrapper = factoryShallowMount({
                milesAbbr: 'test-abbreviation',
            });
            expect(wrapper.html()).toContain('<abbr title="miles">test-abbreviation</abbr>');
        });

        it('should accept an kilometresLabel property', () => {
            const label = 'kilomètres';
            const wrapper = factoryShallowMount({
                kilometresLabel: label,
            });
            expect(wrapper.html()).toContain(label);
        });

        it('should accept an kilometresAbbr property', () => {
            const wrapper = factoryShallowMount({
                kilometresAbbr: 'booya!',
            });
            expect(wrapper.html()).toContain('<abbr title="kilometres">booya!</abbr>');
        });

        it('should call the handleClick method on click of miles button', () => {
            const wrapper = factoryMount();
            wrapper.find('[aria-controls="display_miles"]').trigger('click');

            expect(spyToggleShowMiles).toBeCalled();
            expect(spyToggleShowMiles).toHaveBeenCalledWith(true);
        });

        it('should call the handleClick method on click of kilometres button', () => {
            const wrapper = factoryMount();
            wrapper.find('[aria-controls="display_kilometres"]').trigger('click');

            expect(spyToggleShowMiles).toBeCalled();
            expect(spyToggleShowMiles).toHaveBeenCalledWith(false);
        });
    });
});
