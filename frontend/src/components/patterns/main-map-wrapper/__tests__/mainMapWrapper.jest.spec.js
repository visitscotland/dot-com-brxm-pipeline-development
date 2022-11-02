import { shallowMount } from '@vue/test-utils';
import VsMainMapWrapper from '../MainMapWrapper';
import placesJson from './data/places.json';
import filtersJson from './data/filters.json';

const factoryShallowMount = () => shallowMount(VsMainMapWrapper, {
    slots: {
        openSidePanelText: 'Open panel',
    },
    propsData: {
        placesData: placesJson.features,
        filters: filtersJson,
        selectedItem: 'a4260a0c-9d66-425b-835a-eec833c30a92',
        mapId: 'vs-map',
        currentStage: 0,
    },
});

describe('VsMainMapWrapper', () => {
    it('should render a component with the data-test attribute `vs-main-map-wrapper`', () => {
        const wrapper = factoryShallowMount();
        expect(wrapper.attributes('data-test')).toBe('vs-main-map-wrapper');
    });

    describe(':methods', () => {
        it('should hide the side panel after the `closePanel` method is fired', async() => {
            const wrapper = factoryShallowMount();
            const panel = wrapper.find('[data-test="vs-main-map-wrapper__side-panel"]');

            wrapper.vm.closePanel();

            await wrapper.vm.$nextTick();

            expect(panel.classes('d-none')).toBe(true);
        });

        it('should show the side panel after the `openPanel` method is fired', async() => {
            const wrapper = factoryShallowMount();
            const panel = wrapper.find('[data-test="vs-main-map-wrapper__side-panel"]');

            wrapper.vm.closePanel();

            await wrapper.vm.$nextTick();

            wrapper.vm.openPanel();

            await wrapper.vm.$nextTick();

            expect(panel.classes('d-none')).toBe(false);
        });

        it('should change the `selectedItem` data when the `showDetail` method is fired', async() => {
            const wrapper = factoryShallowMount();

            wrapper.vm.showDetail('test');
            await wrapper.vm.$nextTick();
            expect(wrapper.vm.selectedItem).toBe('test');
        });

        it('should add all places to `activePins` when `showAllPlaces` is fired', async() => {
            const wrapper = factoryShallowMount();
            wrapper.vm.showAllPlaces();
            await wrapper.vm.$nextTick();
            expect(wrapper.vm.activePins.length).toBe(13);
        });

        it('should change the `selectedCategory` data when the `setCategory` method is fired', async() => {
            const wrapper = factoryShallowMount();

            wrapper.vm.setCategory('cities');
            await wrapper.vm.$nextTick();
            expect(wrapper.vm.selectedCategory).toBe('cities');
        });

        it('should change the `currentStage` data when the `setStage` method is fired', async() => {
            const wrapper = factoryShallowMount();

            wrapper.vm.setStage(2);
            await wrapper.vm.$nextTick();
            expect(wrapper.vm.currentStage).toBe(2);
        });

        it('should add all objects from `placesData` prop to `activePins` when `showAllPlaces` is fired', async() => {
            const wrapper = factoryShallowMount();
            wrapper.vm.showAllPlaces();

            await wrapper.vm.$nextTick();
            expect(wrapper.vm.activePins.length).toBe(13);
        });

        it('filters places by a category when `filterPlaces` is fired', async() => {
            const wrapper = factoryShallowMount();
            wrapper.vm.filterPlaces('cities');

            expect(wrapper.vm.activePins.length).toBe(3);
        });
    });

    describe(':slots', () => {
        it('should display the `openMapText` slot', () => {
            const wrapper = factoryShallowMount();
            const openMapBtn = wrapper.find('[data-test="vs-main-map-wrapper__map-toggle"]');

            expect(openMapBtn.text()).toBe('Open panel');
        });
    });
});
