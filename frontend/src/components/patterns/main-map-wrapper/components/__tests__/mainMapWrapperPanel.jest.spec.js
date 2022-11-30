import { shallowMount } from '@vue/test-utils';
import VsMainMapWrapperPanel from '../MainMapWrapperPanel';

const factoryShallowMount = (propsData) => shallowMount(VsMainMapWrapperPanel, {
    slots: {
        closeSidePanelText: 'Close panel',
        backBtnText: 'Back',
        resetSidePanelText: 'Reset panel',
    },
    propsData: {
        ...propsData,
    },
    provide: {
        placesData: [{
            properties: {
                category: {
                    id: 'cities',
                    label: 'Cities',
                },
                title: 'Aberdeen',
                id: 'aberdeen',
            },
        },
        {
            properties: {
                category: {
                    id: 'cities',
                    label: 'Cities',
                },
                title: 'Dundee',
                id: 'dundee',
            },
        }],
        filters: [
            {
                id: 'cities',
                label: 'Cities',
            },
            {
                id: 'towns',
                label: 'Towns',
            },
            {
                id: 'islands',
                label: 'Islands',
            },
            {
                id: 'regions',
                label: 'Regions',
            },
            {
                id: 'featured',
                label: 'Featured',
            },
        ],
    },
});

describe('VsMainMapWrapperPanel', () => {
    it('should render a component with the data-test attribute `vs-main-map-wrapper-panel`', () => {
        const wrapper = factoryShallowMount({
            currentStage: 0,
        });
        expect(wrapper.attributes('data-test')).toBe('vs-main-map-wrapper-panel');
    });

    describe(':methods', () => {
        it('should emit `close-panel` when the close button is clicked', async() => {
            const wrapper = factoryShallowMount({
                currentStage: 0,
            });
            const closeBtn = wrapper.find('[data-test="vs-main-map-wrapper-panel--btn-close"]');

            await closeBtn.trigger('click');

            expect(wrapper.emitted('close-panel')).toBeTruthy();
        });

        it('should emit `set-stage` with a value of currentStage - 1 when the back button is clicked', async() => {
            const wrapper = factoryShallowMount({
                currentStage: 1,
            });
            await wrapper.vm.$nextTick();
            const backBtn = wrapper.find('[data-test="vs-main-map-wrapper-panel--btn-back"]');

            backBtn.trigger('click');

            expect(wrapper.emitted('set-stage')[0]).toEqual([0]);
        });

        it('should emit `set-stage` with a value of 0 when the `resetPanel` method is called', () => {
            const wrapper = factoryShallowMount({
                currentStage: 1,
            });
            wrapper.vm.resetPanel();

            expect(wrapper.emitted('set-stage')[0]).toEqual([0]);
        });

        it('should emit `set-stage` when the `setStage` method is called', () => {
            const wrapper = factoryShallowMount({
                currentStage: 1,
            });
            wrapper.vm.setStage(2);

            expect(wrapper.emitted('set-stage')[0]).toEqual([2]);
        });
    });

    describe(':props', () => {
        it('should display a header with the `categoryHeading` prop value', async() => {
            const wrapper = factoryShallowMount({
                categoryHeading: 'This is a heading',
                currentStage: 0,
            });
            const panelHeading = wrapper.find('[data-test="vs-main-map-wrapper-panel__heading"]');

            expect(panelHeading.text()).toBe('This is a heading');
        });

        it('should not display a header if the `categoryHeading` prop is blank', () => {
            const wrapper = factoryShallowMount({
                currentStage: 0,
            });
            const panelHeading = wrapper.find('[data-test="vs-main-map-categories__heading"]');

            expect(panelHeading.exists()).toBe(false);
        });

        it('should show the correct heading level from the `headingLevel prop', () => {
            const wrapper = factoryShallowMount({
                currentStage: 1,
                headingLevel: '3',
            });
            const headingStub = wrapper.find('vsheading-stub');
            expect(headingStub.attributes('level')).toBe('3');
        });
    });

    describe(':slots', () => {
        it('should display a button with the `closePanelText` slot value', () => {
            const wrapper = factoryShallowMount({
                currentStage: 0,
            });
            const closeBtn = wrapper.find('[data-test="vs-main-map-wrapper-panel--btn-close"]');

            expect(closeBtn.text()).toBe('Close panel');
        });

        it('should display a button with the `backBtnText` slot value', async() => {
            const wrapper = factoryShallowMount({
                currentStage: 1,
            });
            await wrapper.vm.$nextTick();
            const backBtn = wrapper.find('[data-test="vs-main-map-wrapper-panel--btn-back"]');

            expect(backBtn.text()).toBe('Back');
        });

        it('should display a button with the `resetSidePanelText` slot value', () => {
            const wrapper = factoryShallowMount({
                currentStage: 0,
            });
            const closeBtn = wrapper.find('[data-test="vs-main-map-wrapper-panel--btn-reset"]');

            expect(closeBtn.text()).toBe('Reset panel');
        });
    });
});
