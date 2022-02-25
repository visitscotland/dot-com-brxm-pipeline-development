import { shallowMount } from '@vue/test-utils';
import VsToggleButton from '../ToggleButton';

const toggleIdValue = 'toggle-id';

const defaultSlotText = 'Click here to toggle caption';
const toggleIconSlot = 'Toggle icon';

const factoryShallowMount = (propsData) => shallowMount(VsToggleButton, {
    propsData: {
        toggleId: toggleIdValue,
        ...propsData,
    },
    slots: {
        'toggle-icon': toggleIconSlot,
        default: defaultSlotText,
    },
});

describe('VsToggleButton', () => {
    it('should render a component with the data-test attribute `vs-toggle-btn`', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.attributes('data-test')).toBe('vs-toggle-btn');
    });

    describe(':props', () => {
        it('should set correct ID for aria controls with `toggleId` prop', () => {
            const wrapper = factoryShallowMount();
            const toggleCaptionBtn = wrapper.find('.vs-toggle-btn');

            expect(toggleCaptionBtn.attributes('aria-controls')).toBe(toggleIdValue);
        });
    });

    describe(':slots', () => {
        it('renders content in the `toggle-icon` slot', () => {
            const wrapper = factoryShallowMount();

            expect(wrapper.html()).toContain(toggleIconSlot);
        });

        it('renders content in the `default` slot', () => {
            const wrapper = factoryShallowMount();
            expect(wrapper.html()).toContain(defaultSlotText);
        });
    });

    describe(':methods', () => {
        it('emits `toggleImage` when clicked', async() => {
            const wrapper = factoryShallowMount();
            const toggleCaptionBtn = wrapper.find('.vs-toggle-btn');

            toggleCaptionBtn.trigger('click');
            await wrapper.vm.$nextTick();

            expect(wrapper.emitted().toggleAction).toBeTruthy();
        });

        it(':toggleCaption - icon is updated when the caption is toggled on', async() => {
            const wrapper = factoryShallowMount();
            const toggleCaptionBtn = wrapper.find('.vs-toggle-btn');
            await toggleCaptionBtn.trigger('click');

            expect(wrapper.find('vsicon-stub').attributes('name')).toBe('close-circle-filled');
        });

        it(':toggleCaption - caption hide icon when toggled twice', async() => {
            const wrapper = factoryShallowMount();
            wrapper.vm.toggleAction();
            wrapper.vm.toggleAction();
            await wrapper.vm.$nextTick();

            expect(wrapper.html()).toContain(toggleIconSlot);
        });
    });
});
