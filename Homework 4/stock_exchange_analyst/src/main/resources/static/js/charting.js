async function renderChart() {
    try {

        // TODO: don't make function calls if date fields are empty or form is not submitted

        const company = document.getElementById("companyDropdown").value;
        const fromDate = document.getElementById("dateFrom").value;
        const toDate = document.getElementById("dateTo").value;
        const statistic = document.getElementById("statisticDropdown").value;

        const response = await fetch('/api/stockdata', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: new URLSearchParams({company: company, dateFrom: fromDate, dateTo: toDate})
        });

        if (!response.ok) {
            throw new Error("Network entity not OK");
        }

        const data = await response.json();

        console.log(data[0])

        const dataDict = {
            dates: data.map(obs => obs.date),
            lastTradePrice: data.map(obs => obs.values.lastTradePrice),
            max: data.map(obs => obs.values.max),
            min: data.map(obs => obs.values.min),
            avgPrice: data.map(obs => obs.values.avgPrice),
            chg: data.map(obs => obs.values.chg),
            volume: data.map(obs => obs.values.volume),
            turnoverBestMKD: data.map(obs => obs.values.turnoverBestMKD),
            totalTurnoverMKD: data.map(obs => obs.values.totalTurnoverMKD)
        };

        console.log(dataDict);

        updateChart(dataDict, statistic);
    } catch (error) {
        console.error("Error fetching stock data:", error);
    }
}

function updateChart(data, statistic) {
    const ctx = document.getElementById("graphCanvas").getContext("2d");

    if (window.stockChart) {
        window.stockChart.destroy();
    }

    const chartData = {
        labels: data.dates,
        datasets: [{
            label: statistic,
            data: data[statistic],
            borderColor: "#007bff",
            fill: false
        }]
    };

    window.stockChart = new Chart(ctx, {
        type: "line",
        data: chartData,
        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: true
                },
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Date'
                    },
                    ticks: {
                        callback: function (val, index) {
                            const totalTick = data.dates.length;
                            const maxTicks = 5;
                            const tickInterval = Math.floor(totalTick / maxTicks);
                            if (index === 0 || index === totalTick - 1 || index % tickInterval === 0) {
                                return data.dates[index];
                            }
                            return '';
                        }
                    },
                },
                y: {
                    title: {
                        display: true,
                        text: 'Values'
                    }
                }
            }
        }
    });
}

document.addEventListener("DOMContentLoaded", renderChart);
