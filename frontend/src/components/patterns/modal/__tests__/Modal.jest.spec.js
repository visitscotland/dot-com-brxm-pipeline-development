import { shallowMount, createWrapper } from '@vue/test-utils';

import VsModal from '../Modal';

const defaultSlotText = 'Modal text';

const factoryShallowMount = (propsData) => shallowMount(VsModal, {
    slots: {
        default: defaultSlotText,
    },
    propsData: {
        modalId: 'my-modal',
        closeBtnText: 'Close',
        ...propsData,
    },
});

describe('VsModal', () => {
    it('should render a bmodal-stub', () => {
        const wrapper = factoryShallowMount();

        expect(wrapper.element.tagName).toBe('BMODAL-STUB');
    });

    it('should emit `video-controls` when the modal is shown', async() => {
        const wrapper = factoryShallowMount({
            isVideoModal: true,
        });

        const rootWrapper = createWrapper(wrapper.vm.$root);
        rootWrapper.vm.$emit('bv::modal::shown');

        expect(rootWrapper.emitted('video-controls')).toBeTruthy();
    });

    describe(':props', () => {
        it('modalId: sets the correct ID on the modal element', () => {
            const wrapper = factoryShallowMount();
            const modal = wrapper.find('[data-test=vs-modal]');

            expect(modal.attributes('id')).toBe('my-modal');
        });

        it('closeBtnText: sets the text on close button element', () => {
            const wrapper = factoryShallowMount();
            const closeBtn = wrapper.find('[data-test=vs-modal__close-btn]');

            expect(closeBtn.text()).toContain('Close');
        });

        it('isVideoModal: defines static prop on BModal', async() => {
            const wrapper = factoryShallowMount();
            wrapper.setProps({
                isVideoModal: true,
            });
            await wrapper.vm.$nextTick();

            const modalStub = wrapper.find('bmodal-stub').html();

            expect(modalStub).toContain('static="true"');
        });
    });

    describe(':slots', () => {
        it('renders content inserted into default `slot`', () => {
            const wrapper = factoryShallowMount();
            const modal = wrapper.find('[data-test=vs-modal]');

            expect(modal.text()).toContain(defaultSlotText);
        });
    });

    describe(':methods', () => {
        it('modal is closed on close button click', () => {
            const wrapper = factoryShallowMount();
            const mockCloseModal = jest.fn();
            wrapper.vm.hideModal = mockCloseModal;
            const closeBtn = wrapper.find('[data-test=vs-modal__close-btn]');

            closeBtn.trigger('click');
            expect(mockCloseModal).toHaveBeenCalled();
        });
    });
});
