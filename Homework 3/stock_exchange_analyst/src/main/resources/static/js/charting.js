async function renderChart() {
    try {

        // TODO: dont make function calls if date fields are empty or form is not submitted

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

        let dates = data.map(obs => obs.date)
        let tradePrices = data.map(obs => obs.lastTradePrice)
        let maxes = data.map(obs => obs.max)
        let minimums = data.map(obs => obs.min)
        let avgPrices = data.map(obs => obs.avgPrice)
        let changes = data.map(obs => obs.chg)
        let volumes = data.map(obs => obs.volume)
        let turnoversBest = data.map(obs => obs.turnoverBestMKD)
        let turnoversTotal = data.map(obs => obs.totalTurnoverMKD)

        const dataDict = {
            "dates": dates,
            "lastTrade": tradePrices,
            "max": maxes,
            "min": minimums,
            "avgPrice": avgPrices,
            "chg": changes,
            "volume": volumes,
            "turnoverBestMKD": turnoversBest,
            "totalTurnoverMKD": turnoversTotal
        }

        console.log(dataDict)

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

    window.stockChart = new Chart(ctx,
        {
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
                                return ''
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
        }
    );
}

document.addEventListener("DOMContentLoaded", renderChart);
