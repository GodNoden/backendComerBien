package com.comerbien.backend.config;

import com.comerbien.backend.repository.FoodFactRepository;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.comerbien.backend.model.entity.FoodFact;
import com.comerbien.backend.model.enums.MealCategory;

// Actualiza los datos de ejemplo con URLs reales:
@Component
public class FoodFactDataInitializer {

        private final FoodFactRepository foodFactRepository;

        FoodFactDataInitializer(FoodFactRepository foodFactRepository) {
                this.foodFactRepository = foodFactRepository;
        }

        @EventListener(ApplicationReadyEvent.class)
        public void initializeFoodFacts() {
                if (foodFactRepository.count() == 0) {
                        System.out.println("🍎 Initializing sample food facts with source URLs...");

                        // Facts para Desayuno
                        FoodFact breakfastFact1 = new FoodFact(
                                        "Energía Matutina Sustentable",
                                        "Un desayuno balanceado con proteínas y carbohidratos complejos proporciona energía sostenida para toda la mañana y mejora el rendimiento cognitivo.",
                                        "Asociación Mexicana de Nutriología",
                                        MealCategory.DESAYUNO,
                                        "https://www.ammfen.org.mx" // ✅ URL real
                        );
                        breakfastFact1.setKeywords(List.of("huevo", "avena", "fruta", "proteína", "fibra"));

                        FoodFact breakfastFact2 = new FoodFact(
                                        "Proteína del Huevo",
                                        "El huevo contiene proteína de alto valor biológico, colina para la salud cerebral, luteína para la visión y todos los aminoácidos esenciales.",
                                        "Egg Nutrition Center",
                                        MealCategory.DESAYUNO,
                                        "https://www.eggnutritioncenter.org" // ✅ URL real
                        );
                        breakfastFact2.setKeywords(List.of("huevo", "proteína", "colina", "aminoácidos"));

                        // Facts para Comida
                        FoodFact lunchFact1 = new FoodFact(
                                        "Comida Principal Balanceada",
                                        "La comida del mediodía debe incluir proteínas magras, carbohidratos complejos y grasas saludables para mantener niveles estables de energía.",
                                        "INCMNSZ",
                                        MealCategory.COMIDA,
                                        "https://www.incmnsz.mx" // ✅ URL real
                        );
                        lunchFact1.setKeywords(List.of("proteína", "vegetales", "grasas", "balance"));

                        FoodFact lunchFact2 = new FoodFact(
                                        "Beneficios del Pescado",
                                        "El pescado es rico en omega-3, esencial para la salud cerebral, cardiovascular y antiinflamatoria.",
                                        "American Heart Association",
                                        MealCategory.COMIDA,
                                        "https://www.heart.org" // ✅ URL real
                        );
                        lunchFact2.setKeywords(List.of("pescado", "omega3", "proteína", "corazón"));

                        // Facts para ingredientes mexicanos (basados en tu frontend)
                        FoodFact avocadoFact = new FoodFact(
                                        "Aguacate vs Otros Aceites",
                                        "El aceite de aguacate mexicano tiene un punto de humo más alto que el aceite de oliva (271°C vs 190°C), siendo ideal para cocinar a altas temperaturas.",
                                        "INIFAP",
                                        null,
                                        "https://www.gob.mx/inifap" // ✅ URL real
                        );
                        avocadoFact.setKeywords(List.of("aguacate", "grasas", "cocina", "mexicano"));

                        FoodFact chiliFact = new FoodFact(
                                        "Poder Nutricional del Chile",
                                        "Los chiles mexicanos contienen más vitamina C que las naranjas. Un chile habanero tiene 357mg de vitamina C por 100g.",
                                        "UNAM - Instituto de Biología",
                                        null,
                                        "https://www.ib.unam.mx" // ✅ URL real
                        );
                        chiliFact.setKeywords(List.of("chile", "vitamina c", "antioxidante", "mexicano"));

                        FoodFact beanFact = new FoodFact(
                                        "Frijoles: Proteína Completa Mexicana",
                                        "La combinación tradicional mexicana de frijoles con maíz crea una proteína completa con todos los aminoácidos esenciales.",
                                        "Secretaría de Agricultura",
                                        null,
                                        "https://www.gob.mx/agricultura" // ✅ URL real
                        );
                        beanFact.setKeywords(List.of("frijol", "proteína", "maíz", "tradicional"));

                        FoodFact tomatoFact = new FoodFact(
                                        "Lycopene en Tomates",
                                        "Los tomates cocidos liberan más licopeno, un antioxidante que protege contra el cáncer de próstata y mejora la salud cardiovascular.",
                                        "Journal of Nutrition",
                                        null,
                                        "https://academic.oup.com/jn");
                        tomatoFact.setKeywords(
                                        List.of("tomate", "jitomate", "licopeno", "antioxidante", "cáncer", "corazón"));

                        FoodFact garlicFact = new FoodFact(
                                        "Ajo y Salud Cardiovascular",
                                        "El ajo contiene alicina, compuesto que puede reducir la presión arterial y el colesterol LDL. El ajo crudo tiene mayores beneficios.",
                                        "American Journal of Clinical Nutrition",
                                        null,
                                        "https://academic.oup.com/ajcn");
                        garlicFact.setKeywords(List.of("ajo", "ajos", "alicina", "presión arterial", "colesterol",
                                        "cardiovascular"));

                        // Guardar todos los facts
                        foodFactRepository.saveAll(List.of(
                                        breakfastFact1, breakfastFact2, lunchFact1, lunchFact2,
                                        avocadoFact, chiliFact, beanFact));

                        System.out.println(
                                        "✅ " + foodFactRepository.count() + " food facts with source URLs initialized");
                }
        }
}