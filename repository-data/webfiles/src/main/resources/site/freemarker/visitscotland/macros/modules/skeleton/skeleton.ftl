<#macro skeleton>
    <div 
        class="skeleton-site"
        style="opacity: 0.4;
            width: 100vw;
            height: 100vh;
            overflow: hidden;"
    >
        <div
            style="height: 28px;;
                background-color: #700e57;"
        ></div>
        <div
            style="height: 45px;
                background-color: #eeeeee"
        ></div>
        <div
            style="height: 100vh;
                background-color: #bbbbbb;
                position: relative;"
        >
        </div>
        <div
            style="position: absolute;
                bottom: 0;
                left: 10%;
                width: 80%;
                height: 120px;
                background-color: #ffffff;
                z-index: 2;"
        ></div>
    </div>
</#macro>