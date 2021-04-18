<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link rel="icon" href="../../images/favicon.ico" />
</head>

<body class="email">
    <section class="title">
        <h1>
            Here's your summary of the week
        </h1>
    </section>
    <#list days as day>
        <section class="dayRow">
            <h2 class="day">${day.toString()}</h2>
            <div class="daysHeadlines">
                <#list day.headlines as headline>
                    <div class="headline">
                        <img src="${headline.urlLink}/favicon.ico" />
                        ${headline.htmlLink}
                    </div>
                </#list>
            </div>
        </section>
    </#list>
</body>

<style>
    .email {
        background-color: #f2f2f2;
    }

    .headline {
        margin: 20px 0 20px 20px;
    }

    .headline img {
        width: 20px;
        height: 20px;
        margin-right: 5px;
    }

    .headline a {
        font-size: 22px;
        font-family: sans-serif;
    }
</style>

</html>