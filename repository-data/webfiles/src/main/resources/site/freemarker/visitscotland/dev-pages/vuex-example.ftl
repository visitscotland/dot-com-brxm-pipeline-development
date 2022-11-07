<#include "../../include/imports.ftl">
<#include "../../frontend/components/vs-vue-x-example.ftl">
<#include "../../frontend/stores/vs-store-example-store.ftl">
<#include "../../frontend/components/vs-container.ftl">
<#include "../../frontend/components/vs-row.ftl">
<#include "../../frontend/components/vs-col.ftl">


<@hst.headContribution category="htmlBodyEndScripts">
	<script>
		vs.stores.VsStoreExampleStore.subscribe((mutation, state) => {
            const trackerElement = document.getElementById("vue-x-example-tracker");

            trackerElement.innerHTML = state.example.count
        })
	</script>
</@hst.headContribution>

<vs-container style="margin-bottom: 32px">
    <vs-row>
        <vs-col>
            <p>This is an example of how data held in a Vuex store can be shared between component instances as well as outside of any component, if desired. There are 2 instances of the Vuex Example component (vs-vuex-example) rendered on this page. The vs-vuex-example component uses the exampleStore Vuex store to keep a count, which incremenets when the button inside the component is clicked.</p>
            <p>Note that this would also work if the components were different types, as long as those components imported the same store.</p>
            <p>The exampleStore Vuex store is also imported directly into this freemarker template, which subscribes to changes in the Vuex store and updates
            the count display in the left-hand box when the count is incremeneted.</p>
        </vs-col>
    </vs-row>
    <vs-row>
        <div style="padding:12px;background-color:#eee;display:inline-block" class="col-4">
            <h4>Tracking count outside of a Vue component</h4>
            <strong id="vue-x-example-tracker">0</strong>
        </div>

        <vs-vue-x-example 
            style="display:inline-block"
            class="col-4"
            title="Example Vuex-using component instance 1"
        >Increment</vs-vue-x-example>
        <vs-vue-x-example 
            style="display:inline-block" 
            class="col-4"
            title="Example Vuex-using component instance 2"
        >Increment</vs-vue-x-example>

    </vs-row>
</vs-container>

