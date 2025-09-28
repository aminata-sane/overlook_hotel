package com.example.demo.controller;

import com.example.demo.model.Panier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PanierController {

    // Obtenir le nombre d'items dans le panier (API)
    @GetMapping("/panier/count")
    @ResponseBody
    public Map<String, Object> obtenirCompteurPanier(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        Panier panier = (Panier) session.getAttribute("panier");
        
        if (panier != null && !panier.estVide()) {
            response.put("success", true);
            response.put("nombreItems", panier.getNombreItems());
            response.put("prixTotal", panier.getPrixTotal());
        } else {
            response.put("success", true);
            response.put("nombreItems", 0);
            response.put("prixTotal", 0.0);
        }
        
        return response;
    }

    // Afficher la page du panier
    @GetMapping("/panier")
    public String afficherPanier(HttpSession session, Model model) {
        Panier panier = (Panier) session.getAttribute("panier");
        
        if (panier == null) {
            panier = new Panier();
            session.setAttribute("panier", panier);
        }
        
        model.addAttribute("panier", panier);
        return "panier"; // templates/panier.html
    }

    // Supprimer un item du panier
    @PostMapping("/panier/supprimer/{index}")
    public String supprimerItem(@PathVariable int index, HttpSession session) {
        Panier panier = (Panier) session.getAttribute("panier");
        
        if (panier != null) {
            panier.supprimerItem(index);
        }
        
        return "redirect:/panier";
    }

    // Vider le panier
    @PostMapping("/panier/vider")
    public String viderPanier(HttpSession session) {
        Panier panier = (Panier) session.getAttribute("panier");
        
        if (panier != null) {
            panier.vider();
        }
        
        return "redirect:/panier";
    }
}
