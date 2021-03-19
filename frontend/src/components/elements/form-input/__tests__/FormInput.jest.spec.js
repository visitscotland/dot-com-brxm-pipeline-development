import { mount, shallowMount } from '@vue/test-utils';
import VsFormInput from '../FormInput';

const factoryShallowMount = (propsData) => shallowMount(VsFormInput, {
    propsData: {
        ...propsData,
    },
});

const factoryMount = (propsData) => mount(VsFormInput, {
    propsData: {
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsFormInput', () => {
    it('should render a bform-input-stub', () => {
        expect(wrapper.element.tagName).toBe('BFORMINPUT-STUB');
    });

    describe(':props', () => {
        it('size - should be `md` by default', () => {
            expect(wrapper.attributes('size')).toBe('md');
        });

        it('size - should accept and render a `size` property', () => {
            const testSize = 'lg';
            const modifiedWrapper = factoryMount({
                size: testSize,
            });

            expect(modifiedWrapper.classes()).toContain(`form-control-${testSize}`);
        });

        it('value - should accept and render a `value` property', async() => {
            const testValue = 'Test Value';
            wrapper.setProps({
                value: testValue,
            });

            await wrapper.vm.$nextTick();

            expect(wrapper.vm.inputVal).toBe(testValue);
        });
    });

    describe(':events', () => {
        it('it should emit an `input` event when `value` changes', async() => {
            const testValue = 'Test Value';
            const modifiedWrapper = factoryMount();
            modifiedWrapper.setProps({
                value: testValue,
            });

            await wrapper.vm.$nextTick();

            expect(modifiedWrapper.emitted('input')).toBeTruthy();
        });

        it('it should emit an `input` event with the new value when `value` changes', async() => {
            const testValue = 'Test Value';
            const modifiedWrapper = factoryMount();
            modifiedWrapper.setProps({
                value: testValue,
            });

            await wrapper.vm.$nextTick();

            expect(modifiedWrapper.emitted('input')[0]).toEqual([testValue]);
        });
    });
});
