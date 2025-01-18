package mk.finki.ukim.mk.stock_exchange_analyst.web.controller;

import mk.finki.ukim.mk.stock_exchange_analyst.service.PredictionService;
import mk.finki.ukim.mk.stock_exchange_analyst.service.StockService;
import mk.finki.ukim.mk.stock_exchange_analyst.service.exception.NoSuchCompanyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/lstm")
public class LSTMController {

    private final StockService stockService;
    private final PredictionService predictionService;

    public LSTMController(StockService stockService, PredictionService predictionService) {
        this.stockService = stockService;
        this.predictionService = predictionService;
    }

    @GetMapping
    public String getLSTMPage(
            @RequestParam (required = false) String company,
            Model model) {

        model.addAttribute("companies", stockService.listCompanies());

        if (company != null) {

            try {
                predictionService.getPredictionsOf(company);
            } catch (NoSuchCompanyException exc) {
                model.addAttribute("error", exc.getMessage());
                return "lstm";
            }

            model.addAttribute("prediction", predictionService.getPredictionsOf(company));
            model.addAttribute("companyViewing", company);
        }

        return "lstm";
    }

    @PostMapping
    public String predict(@RequestParam String company) {
        return "redirect:/lstm?company=" + company;
    }

}
