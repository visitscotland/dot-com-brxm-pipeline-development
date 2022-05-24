import { shallowMount } from '@vue/test-utils';
import VsInput from '../Input';

const factoryShallowMount = (propsData) => shallowMount(VsInput, {
    propsData: {
        fieldName: 'testname',
        type: 'text',
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsInput', () => {
    it('should render a bform-input-stub', () => {
        expect(wrapper.attributes('data-test')).toBe('vs-input');
    });

    describe(':props', () => {
        it('value - should accept and render a `value` property', async() => {
            const testValue = 'Test Value';
            wrapper.setProps({
                value: testValue,
            });

            await wrapper.vm.$nextTick();

            expect(wrapper.vm.inputVal).toBe(testValue);
        });

        it('value - should accept and render a `fieldName` property', async() => {
            wrapper.setProps({
                fieldName: 'testValue',
            });

            await wrapper.vm.$nextTick();

            expect(wrapper.find('.vs-input').attributes('name')).toBe('testValue');
        });

        it('value - should accept and render a `type` property', async() => {
            expect(wrapper.find('.vs-input').attributes('type')).toBe('text');
        });

        it('should show the clear button when there is a value if the `closeButtonText` prop is defined', async() => {
            wrapper.setProps({
                clearButtonText: 'clear text',
            });

            wrapper.setData({
                inputVal: 'test',
            });
            await wrapper.vm.$nextTick();

            expect(wrapper.find('[data-test="input-clear-button"]').text()).toBe('clear text');
        });
    });

    describe(':computed', () => {
        it('should add a required prop when its included in valdiation rules', async() => {
            wrapper.setProps({
                validationRules: {
                    required: true,
                },
            });

            await wrapper.vm.$nextTick();

            expect(wrapper.find('.vs-input').attributes('required')).toBe('true');
        });

        it('should display a validation message if validation fails', async() => {
            wrapper.setProps({
                invalid: true,
                validationRules: {
                    required: true,
                },
                validationMessages: {
                    required: 'This is required',
                },
            });

            wrapper.vm.manualValidate();

            await wrapper.vm.$nextTick();

            expect(wrapper.html()).toContain('This is required');
        });
    });

    describe(':methods', () => {
        it('clears the `inputVal` data when the `clearInput` method is triggered', async() => {
            wrapper.setData({
                inputVal: 'test',
            });

            await wrapper.vm.$nextTick();

            wrapper.vm.clearInput();

            expect(wrapper.vm.inputVal).toBe('');
        });
    });

    describe(':watchers', () => {
        it('should call the `manualValidate` method when `triggerValidation` is emitted', async() => {
            const mockedMethod = jest.spyOn(wrapper.vm, 'manualValidate');

            wrapper.setProps({
                triggerValidate: true,
            });

            await wrapper.vm.$nextTick();

            expect(mockedMethod).toBeCalled();
        });
    });
});
