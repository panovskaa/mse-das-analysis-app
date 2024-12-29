package mk.finki.ukim.mk.stock_exchange_analyst.web.controller;

import mk.finki.ukim.mk.stock_exchange_analyst.service.SentimentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/fundamental")
public class FundamentalController {

    private final SentimentService sentimentService;

    public FundamentalController(SentimentService sentimentService) {
        this.sentimentService = sentimentService;
    }

    @GetMapping
    public String getFundamentalAnalysisPage(
            @RequestParam (required = false) String company,
            Model model) {

        model.addAttribute("companies", sentimentService.getFullCompanyNames());

        if (company != null) {
            model.addAttribute("companySelected", company);
            model.addAttribute("sentiment", sentimentService.getSentimentOf(company));
        }

        return "fundamental";
    }

    @PostMapping
    public String getSentiment(@RequestParam String company) {
        return "redirect:/fundamental?company=" + company;
    }
}
