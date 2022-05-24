import { shallowMount } from '@vue/test-utils';

import VsSelect from '../Select';

const factoryShallowMount = (propsData) => shallowMount(VsSelect, {
    propsData: {
        fieldName: 'testselect',
        options: [
            {
                text: 'Item 1',
                value: 'first',
            },
            {
                text: 'Item 2',
                value: 'second',
            },
        ],
        ...propsData,
    },
});

let wrapper;
beforeEach(() => {
    wrapper = factoryShallowMount();
});

describe('VsSelect', () => {
    it('should render a BFormSelect-stub', () => {
        const selectStub = wrapper.findAll('BFORMSELECT-STUB');

        expect(selectStub.exists()).toBe(true);
    });

    describe(':props', () => {
        it('should render the element with the `fieldName` prop as the name attribute', () => {
            expect(wrapper.find('[data-test="vs-select"]').attributes('name')).toBe('testselect');
        });

        it('value - should accept and render a `fieldName` property', async() => {
            wrapper.setProps({
                fieldName: 'testValue',
            });

            await wrapper.vm.$nextTick();

            expect(wrapper.find('.vs-select__element').attributes('name')).toBe('testValue');
        });

        it('value - should accept and render a `value` property', async() => {
            const testValue = 'first';
            wrapper.setProps({
                value: testValue,
            });

            await wrapper.vm.$nextTick();

            expect(wrapper.vm.inputVal).toBe(testValue);
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

            expect(wrapper.find('.vs-select__element').attributes('required')).toBe('true');
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
