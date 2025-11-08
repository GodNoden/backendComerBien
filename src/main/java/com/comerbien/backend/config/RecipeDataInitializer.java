package com.comerbien.backend.config;

import com.comerbien.backend.model.entity.Recipe;
import com.comerbien.backend.model.entity.User;
import com.comerbien.backend.model.enums.Difficulty;
import com.comerbien.backend.model.enums.MealCategory;
import com.comerbien.backend.model.enums.Tag;
import com.comerbien.backend.repository.RecipeRepository;
import com.comerbien.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@DependsOn("userInitializer")
public class RecipeDataInitializer {

        private static final Logger logger = LoggerFactory.getLogger(RecipeDataInitializer.class);

        @Autowired
        private RecipeRepository recipeRepository;

        @Autowired
        private UserRepository userRepository;

        // Datos para 50 recetas
        private static final List<RecipeData> RECIPE_DATA = Arrays.asList(
                        // ===== DESAYUNO (12) =====
                        new RecipeData("Tostadas de Aguacate con Huevo", "10 min", Difficulty.FÁCIL,
                                        MealCategory.DESAYUNO, 320, 12, 28, 18,
                                        List.of(Tag.RÁPIDA, Tag.VEGETARIANA),
                                        List.of("2 rebanadas de pan integral", "1 aguacate maduro", "2 huevos",
                                                        "Sal y pimienta", "Cilantro fresco"),
                                        "1. Tuesta el pan. 2. Machaca el aguacate y úntalo. 3. Fríe o escalfa los huevos. 4. Coloca sobre el aguacate. 5. Sazona y decora con cilantro."),

                        new RecipeData("Smoothie Verde Energizante", "5 min", Difficulty.FÁCIL, MealCategory.DESAYUNO,
                                        220, 8, 40, 3,
                                        List.of(Tag.ALTA_FIBRA, Tag.VEGETARIANA),
                                        List.of("1 taza de espinacas", "1 plátano congelado", "1/2 taza de piña",
                                                        "1/2 taza de leche de almendra", "1 cda miel"),
                                        "1. Licúa todos los ingredientes. 2. Sirve inmediatamente."),

                        new RecipeData("Huevos Revueltos con Espinacas", "12 min", Difficulty.FÁCIL,
                                        MealCategory.DESAYUNO, 280, 18, 10, 19,
                                        List.of(Tag.RÁPIDA, Tag.ALTA_PROTEINA),
                                        List.of("3 huevos", "1 taza de espinacas", "1/4 taza de leche",
                                                        "1 cda mantequilla", "Sal y pimienta"),
                                        "1. Bate huevos con leche. 2. Saltea espinacas en mantequilla. 3. Vierte los huevos y cocina revolviendo."),

                        new RecipeData("Avena con Frutas y Nueces", "7 min", Difficulty.FÁCIL, MealCategory.DESAYUNO,
                                        310, 10, 45, 12,
                                        List.of(Tag.ALTA_FIBRA, Tag.VEGETARIANA),
                                        List.of("1/2 taza de avena", "1 taza de leche", "1/2 plátano", "1 cda nueces",
                                                        "Canela al gusto"),
                                        "1. Cocina avena con leche. 2. Agrega frutas y nueces. 3. Espolvorea canela."),

                        new RecipeData("Chilaquiles Verdes Light", "20 min", Difficulty.MEDIO, MealCategory.DESAYUNO,
                                        380, 15, 50, 14,
                                        List.of(Tag.RÁPIDA, Tag.VEGETARIANA),
                                        List.of("100g de totopos", "1 taza de salsa verde", "1/4 taza de queso fresco",
                                                        "Crema light", "Cebolla picada"),
                                        "1. Calienta salsa verde. 2. Agrega totopos y cocina 2 min. 3. Sirve con queso, crema y cebolla."),

                        new RecipeData("Tortilla de Patatas Tradicional", "25 min", Difficulty.MEDIO,
                                        MealCategory.DESAYUNO, 420, 14, 35, 24,
                                        List.of(Tag.VEGETARIANA, Tag.ALTA_FIBRA),
                                        List.of("3 huevos", "2 patatas medianas", "1/2 cebolla", "Aceite de oliva",
                                                        "Sal"),
                                        "1. Fríe patatas y cebolla en aceite. 2. Bate huevos, mezcla. 3. Cocina a fuego lento hasta cuajar."),

                        new RecipeData("Batido de Proteína de Chocolate", "5 min", Difficulty.FÁCIL,
                                        MealCategory.DESAYUNO, 290, 32, 22, 7,
                                        List.of(Tag.ALTA_PROTEINA, Tag.BAJO_EN_CALORIAS),
                                        List.of("1 scoop proteína chocolate", "1 plátano", "1/2 taza leche",
                                                        "1 cda cacao sin azúcar", "Hielo"),
                                        "1. Licúa todos los ingredientes. 2. Disfruta post-entrenamiento o en el desayuno."),

                        new RecipeData("Panquecas de Avena", "15 min", Difficulty.FÁCIL, MealCategory.DESAYUNO, 270, 12,
                                        40, 8,
                                        List.of(Tag.GLUTEN_FREE, Tag.VEGETARIANA),
                                        List.of("1/2 taza avena molida", "1 huevo", "1/2 plátano", "1/2 taza leche",
                                                        "Polvo para hornear"),
                                        "1. Mezcla todos los ingredientes. 2. Cocina en sartén antiadherente. 3. Sirve con frutas."),

                        new RecipeData("Desayuno de Yogur con Granola", "5 min", Difficulty.FÁCIL,
                                        MealCategory.DESAYUNO, 340, 14, 48, 10,
                                        List.of(Tag.RÁPIDA, Tag.VEGETARIANA),
                                        List.of("1 taza yogur natural", "1/4 taza granola", "1/2 taza fresas",
                                                        "1 cda miel"),
                                        "1. Sirve yogur en un bowl. 2. Agrega granola y frutas. 3. Endulza con miel."),

                        new RecipeData("Enfrijoladas Sencillas", "15 min", Difficulty.FÁCIL, MealCategory.DESAYUNO, 360,
                                        16, 52, 8,
                                        List.of(Tag.VEGETARIANA, Tag.RÁPIDA),
                                        List.of("2 tortillas de maíz", "1/2 taza frijoles refritos", "Queso fresco",
                                                        "Cebolla, cilantro", "Crema"),
                                        "1. Calienta tortillas. 2. Báñalas en frijoles calientes. 3. Dobla y sirve con queso, cebolla y crema."),

                        new RecipeData("Molletes de Frijol", "12 min", Difficulty.FÁCIL, MealCategory.DESAYUNO, 330, 14,
                                        48, 9,
                                        List.of(Tag.RÁPIDA, Tag.VEGETARIANA),
                                        List.of("2 bolillos", "1/2 taza frijoles", "Queso Oaxaca", "Salsa verde"),
                                        "1. Parte bolillos a la mitad. 2. Unta frijoles. 3. Agrega queso y gratina. 4. Acompaña con salsa."),

                        new RecipeData("Batido de Calabaza y Canela", "8 min", Difficulty.FÁCIL, MealCategory.DESAYUNO,
                                        210, 7, 38, 4,
                                        List.of(Tag.VEGETARIANA, Tag.ALTA_FIBRA),
                                        List.of("1/2 taza puré calabaza", "1/2 plátano", "1/2 taza leche",
                                                        "1/2 cda canela", "Miel al gusto"),
                                        "1. Licúa todos los ingredientes. 2. Sirve frío o tibio."),

                        // ===== COMIDA (18) =====
                        new RecipeData("Ensalada César con Pollo", "15 min", Difficulty.FÁCIL, MealCategory.COMIDA, 420,
                                        22, 12, 30,
                                        List.of(Tag.RÁPIDA, Tag.ALTA_PROTEINA),
                                        List.of("Lechuga romana", "Pechuga a la parrilla", "Crutones", "Parmesano",
                                                        "Aderezo César light"),
                                        "1. Corta pollo y lechuga. 2. Mezcla con crutones y queso. 3. Agrega aderezo al servir."),

                        new RecipeData("Bowl de Quinoa y Verduras Asadas", "35 min", Difficulty.MEDIO,
                                        MealCategory.COMIDA, 480, 16, 58, 22,
                                        List.of(Tag.ALTA_FIBRA, Tag.VEGETARIANA),
                                        List.of("1 taza quinoa cocida", "Calabacín", "Pimiento rojo", "Cebolla morada",
                                                        "Aceite oliva, limón, comino"),
                                        "1. Hornea verduras con especias. 2. Sirve sobre quinoa. 3. Rocía con limón."),

                        new RecipeData("Sopa de Lentejas", "40 min", Difficulty.MEDIO, MealCategory.COMIDA, 320, 18, 42,
                                        6,
                                        List.of(Tag.ALTA_FIBRA, Tag.VEGETARIANA),
                                        List.of("1 taza lentejas", "Zanahoria", "Apio", "Cebolla", "Ajo", "Comino",
                                                        "Laurel"),
                                        "1. Sofríe verduras. 2. Agrega lentejas y agua. 3. Cocina 30-35 min. 4. Sazona al gusto."),

                        new RecipeData("Tacos de Pescado al Cilantro", "25 min", Difficulty.MEDIO, MealCategory.COMIDA,
                                        410, 24, 38, 16,
                                        List.of(Tag.RÁPIDA, Tag.ALTA_PROTEINA),
                                        List.of("Filete de pescado blanco", "Tortillas de maíz", "Repollo morado",
                                                        "Crema", "Salsa de chipotle"),
                                        "1. Marina pescado en jugo de limón y cilantro. 2. Asa. 3. Sirve en tortillas con repollo y crema."),

                        new RecipeData("Arroz con Verduras y Huevo", "20 min", Difficulty.FÁCIL, MealCategory.COMIDA,
                                        390, 14, 52, 14,
                                        List.of(Tag.RÁPIDA, Tag.ALTA_PROTEINA),
                                        List.of("1 taza arroz cocido", "Zanahoria", "Chícharos", "Huevo",
                                                        "Salsa de soya"),
                                        "1. Saltea verduras. 2. Agrega arroz y huevo revuelto. 3. Sazona con soya."),

                        new RecipeData("Pasta Integral con Pesto", "20 min", Difficulty.FÁCIL, MealCategory.COMIDA, 470,
                                        16, 62, 18,
                                        List.of(Tag.RÁPIDA, Tag.VEGETARIANA),
                                        List.of("150g pasta integral", "Pesto casero", "Cerezas", "Queso parmesano",
                                                        "Aceite de oliva"),
                                        "1. Cuece pasta. 2. Mezcla con pesto y cerezas. 3. Rocía con aceite y queso."),

                        new RecipeData("Pozole Verde Light", "50 min", Difficulty.DIFÍCIL, MealCategory.COMIDA, 380, 28,
                                        30, 12,
                                        List.of(Tag.ALTA_PROTEINA, Tag.VEGETARIANA),
                                        List.of("Pechuga de pollo", "Maíz pozolero", "Salsa verde", "Lechuga", "Rábano",
                                                        "Orégano"),
                                        "1. Cuece pollo y maíz. 2. Agrega salsa verde. 3. Sirve con lechuga, rábano y orégano."),

                        new RecipeData("Ensalada de Nopales", "15 min", Difficulty.FÁCIL, MealCategory.COMIDA, 180, 8,
                                        22, 7,
                                        List.of(Tag.ALTA_FIBRA, Tag.VEGETARIANA),
                                        List.of("2 nopales cocidos", "Tomate", "Cebolla", "Queso panela",
                                                        "Limon, cilantro"),
                                        "1. Pica nopales, tomate y cebolla. 2. Mezcla con queso. 3. Aliña con limón y cilantro."),

                        new RecipeData("Hamburguesa Vegetal de Frijol", "25 min", Difficulty.MEDIO, MealCategory.COMIDA,
                                        420, 18, 48, 16,
                                        List.of(Tag.VEGETARIANA, Tag.ALTA_FIBRA),
                                        List.of("1 taza frijoles negros", "Avena", "Cebolla", "Ajo", "Especias",
                                                        "Pan integral"),
                                        "1. Machaca frijoles con avena. 2. Forma hamburguesas. 3. Asa y sirve en pan con aguacate."),

                        new RecipeData("Sopa de Pollo con Fideos", "30 min", Difficulty.MEDIO, MealCategory.COMIDA, 340,
                                        22, 38, 8,
                                        List.of(Tag.RÁPIDA, Tag.ALTA_PROTEINA),
                                        List.of("Pechuga de pollo", "Fideos", "Zanahoria", "Apio", "Cebolla",
                                                        "Perejil"),
                                        "1. Cocina pollo en agua. 2. Agrega verduras y fideos. 3. Sazona y cocina 15 min."),

                        new RecipeData("Guiso de Lentejas con Arroz", "35 min", Difficulty.MEDIO, MealCategory.COMIDA,
                                        430, 20, 60, 10,
                                        List.of(Tag.ALTA_FIBRA, Tag.VEGETARIANA),
                                        List.of("Lentejas", "Arroz", "Tomate", "Cebolla", "Ajo", "Pimentón"),
                                        "1. Sofríe verduras. 2. Agrega lentejas y agua. 3. Cocina 25 min. 4. Sirve con arroz blanco."),

                        new RecipeData("Tostadas de Tinga de Pollo", "20 min", Difficulty.MEDIO, MealCategory.COMIDA,
                                        400, 24, 42, 15,
                                        List.of(Tag.RÁPIDA, Tag.ALTA_PROTEINA),
                                        List.of("Pechuga deshebrada", "Salsa de chipotle", "Tostadas", "Lechuga",
                                                        "Crema", "Queso"),
                                        "1. Cocina pollo en salsa de chipotle. 2. Sirve sobre tostadas con lechuga, crema y queso."),

                        new RecipeData("Wrap de Pollo y Aguacate", "12 min", Difficulty.FÁCIL, MealCategory.COMIDA, 450,
                                        26, 40, 20,
                                        List.of(Tag.RÁPIDA, Tag.ALTA_PROTEINA),
                                        List.of("Tortilla integral", "Pechuga a la parrilla", "Aguacate", "Espinacas",
                                                        "Mostaza Dijon"),
                                        "1. Extiende mostaza en tortilla. 2. Agrega pollo, aguacate y espinacas. 3. Enrolla y corta."),

                        new RecipeData("Ceviche de Pescado Clásico", "20 min + reposo", Difficulty.MEDIO,
                                        MealCategory.COMIDA, 280, 26, 18, 8,
                                        List.of(Tag.ALTA_PROTEINA, Tag.BAJO_EN_CALORIAS),
                                        List.of("Pescado blanco", "Jugo de limón", "Cebolla morada", "Chile serrano",
                                                        "Cilantro", "Agua de coco (opcional)"),
                                        "1. Corta pescado en cubos. 2. Marina en limón 15 min. 3. Agrega cebolla, chile y cilantro."),

                        new RecipeData("Ensalada de Quinoa y Aguacate", "15 min", Difficulty.FÁCIL, MealCategory.COMIDA,
                                        380, 12, 48, 20,
                                        List.of(Tag.ALTA_FIBRA, Tag.VEGETARIANA),
                                        List.of("Quinoa cocida", "Aguacate", "Tomate cherry", "Pepino",
                                                        "Limon, aceite oliva"),
                                        "1. Mezcla todos los ingredientes. 2. Aliña con limón y aceite."),

                        new RecipeData("Sopa de Zanahoria y Jengibre", "25 min", Difficulty.FÁCIL, MealCategory.COMIDA,
                                        160, 4, 28, 5,
                                        List.of(Tag.BAJO_EN_CALORIAS, Tag.ALTA_FIBRA),
                                        List.of("Zanahorias", "Jengibre", "Caldo vegetal", "Cúrcuma",
                                                        "Leche de coco (opcional)"),
                                        "1. Sofríe jengibre. 2. Agrega zanahorias y caldo. 3. Licúa y sirve caliente."),

                        new RecipeData("Tacos de Hongos Portobello", "20 min", Difficulty.MEDIO, MealCategory.COMIDA,
                                        320, 10, 42, 14,
                                        List.of(Tag.VEGETARIANA, Tag.RÁPIDA),
                                        List.of("Hongos portobello", "Tortillas", "Cebolla asada", "Queso",
                                                        "Salsa verde"),
                                        "1. Asa los hongos. 2. Sirve en tortillas con cebolla, queso y salsa."),

                        new RecipeData("Bowl de Arroz Integral y Frijoles", "20 min", Difficulty.FÁCIL,
                                        MealCategory.COMIDA, 410, 16, 68, 10,
                                        List.of(Tag.ALTA_FIBRA, Tag.VEGETARIANA),
                                        List.of("Arroz integral", "Frijoles negros", "Aguacate", "Tomate",
                                                        "Cilantro, limón"),
                                        "1. Sirve arroz y frijoles. 2. Agrega aguacate y tomate. 3. Aliña con limón y cilantro."),

                        // ===== CENA (12) =====
                        new RecipeData("Salmón al Horno con Espárragos", "25 min", Difficulty.MEDIO, MealCategory.CENA,
                                        420, 34, 12, 26,
                                        List.of(Tag.ALTA_PROTEINA, Tag.BAJO_EN_GRASAS),
                                        List.of("Filete de salmón", "Espárragos", "Aceite de oliva", "Limón",
                                                        "Ajo, romero"),
                                        "1. Coloca salmón y espárragos en charola. 2. Aliña con aceite, limón y especias. 3. Hornea 20 min."),

                        new RecipeData("Crema de Calabacín", "30 min", Difficulty.FÁCIL, MealCategory.CENA, 180, 6, 22,
                                        8,
                                        List.of(Tag.BAJO_EN_CALORIAS, Tag.VEGETARIANA),
                                        List.of("Calabacines", "Cebolla", "Caldo vegetal", "Leche light",
                                                        "Nuez moscada"),
                                        "1. Sofríe cebolla. 2. Agrega calabacín y caldo. 3. Cocina 15 min. 4. Licúa con leche."),

                        new RecipeData("Omelette de Verduras", "15 min", Difficulty.FÁCIL, MealCategory.CENA, 260, 18,
                                        8, 18,
                                        List.of(Tag.RÁPIDA, Tag.ALTA_PROTEINA, Tag.VEGETARIANA),
                                        List.of("3 huevos", "Pimiento", "Espinacas", "Cebolla", "Queso bajo grasa"),
                                        "1. Saltea verduras. 2. Vierte huevos batidos. 3. Agrega queso y dobla."),

                        new RecipeData("Sopa Miso con Tofu", "15 min", Difficulty.FÁCIL, MealCategory.CENA, 190, 12, 16,
                                        9,
                                        List.of(Tag.BAJO_EN_CALORIAS, Tag.VEGETARIANA),
                                        List.of("Pasta miso", "Tofu", "Alga wakame", "Cebollín", "Agua"),
                                        "1. Calienta agua. 2. Disuelve miso. 3. Agrega tofu y alga. 4. Cocina 2 min. 5. Decora con cebollín."),

                        new RecipeData("Pechuga de Pollo a la Mostaza", "25 min", Difficulty.MEDIO, MealCategory.CENA,
                                        380, 36, 10, 20,
                                        List.of(Tag.ALTA_PROTEINA, Tag.RÁPIDA),
                                        List.of("Pechugas", "Mostaza Dijon", "Miel", "Ajo", "Especias",
                                                        "Papas al horno"),
                                        "1. Mezcla mostaza, miel y ajo. 2. Marina pollo. 3. Hornea 20 min con papas."),

                        new RecipeData("Tortilla de Berenjenas", "30 min", Difficulty.MEDIO, MealCategory.CENA, 290, 14,
                                        20, 18,
                                        List.of(Tag.VEGETARIANA, Tag.ALTA_FIBRA),
                                        List.of("Berenjenas", "Huevos", "Cebolla", "Tomate", "Aceite de oliva"),
                                        "1. Hornea berenjenas. 2. Sofríe cebolla y tomate. 3. Mezcla con huevos y cocina como tortilla."),

                        new RecipeData("Ensalada de Atún y Arroz", "12 min", Difficulty.FÁCIL, MealCategory.CENA, 350,
                                        22, 32, 14,
                                        List.of(Tag.RÁPIDA, Tag.ALTA_PROTEINA),
                                        List.of("Atún en agua", "Arroz cocido", "Pimiento", "Cebolla",
                                                        "Mayonesa light, mostaza"),
                                        "1. Mezcla todos los ingredientes. 2. Refrigera 10 min. 3. Sirve frío."),

                        new RecipeData("Caldo de Verduras con Huevo", "20 min", Difficulty.FÁCIL, MealCategory.CENA,
                                        200, 12, 18, 8,
                                        List.of(Tag.BAJO_EN_CALORIAS, Tag.VEGETARIANA),
                                        List.of("Caldo vegetal", "Zanahoria", "Apio", "Huevo", "Perejil"),
                                        "1. Cocina verduras en caldo. 2. Bate huevo y vierte en el caldo caliente. 3. Decora con perejil."),

                        new RecipeData("Quesadillas de Champiñones", "15 min", Difficulty.FÁCIL, MealCategory.CENA, 340,
                                        16, 32, 16,
                                        List.of(Tag.VEGETARIANA, Tag.RÁPIDA),
                                        List.of("Tortillas de maíz", "Champiñones", "Queso Oaxaca", "Cebolla", "Salsa"),
                                        "1. Saltea champiñones con cebolla. 2. Coloca en tortilla con queso. 3. Dobla y gratina."),

                        new RecipeData("Sopa de Tomate Asado", "35 min", Difficulty.MEDIO, MealCategory.CENA, 170, 5,
                                        24, 7,
                                        List.of(Tag.BAJO_EN_CALORIAS, Tag.VEGETARIANA),
                                        List.of("Tomates asados", "Ajo asado", "Caldo vegetal", "Albahaca",
                                                        "Leche de coco"),
                                        "1. Hornea tomates y ajo. 2. Licúa con caldo y leche. 3. Cocina 10 min. 4. Decora con albahaca."),

                        new RecipeData("Huevos al Plato con Espinacas", "18 min", Difficulty.FÁCIL, MealCategory.CENA,
                                        270, 16, 12, 18,
                                        List.of(Tag.RÁPIDA, Tag.ALTA_PROTEINA),
                                        List.of("Huevos", "Espinacas", "Tomate", "Cebolla", "Queso feta"),
                                        "1. Sofríe cebolla y tomate. 2. Agrega espinacas. 3. Casca huevos y cocina. 4. Espolvorea queso."),

                        new RecipeData("Bowl de Lentejas y Batata", "30 min", Difficulty.MEDIO, MealCategory.CENA, 400,
                                        18, 60, 12,
                                        List.of(Tag.ALTA_FIBRA, Tag.VEGETARIANA),
                                        List.of("Lentejas cocidas", "Batata asada", "Espinacas", "Aguacate",
                                                        "Aliño de limón"),
                                        "1. Asa batata. 2. Mezcla con lentejas y espinacas. 3. Agrega aguacate y aliño."),

                        // ===== SNACK (8) =====
                        new RecipeData("Batido de Plátano y Cacahuate", "5 min", Difficulty.FÁCIL, MealCategory.SNACK,
                                        310, 12, 36, 14,
                                        List.of(Tag.BAJO_EN_CALORIAS, Tag.VEGETARIANA),
                                        List.of("1 plátano", "1 cda mantequilla de cacahuate", "1/2 taza leche",
                                                        "Hielo"),
                                        "1. Licúa todos los ingredientes. 2. Disfruta como snack o post-entrenamiento."),

                        new RecipeData("Galletas de Avena y Plátano", "25 min", Difficulty.FÁCIL, MealCategory.SNACK,
                                        180, 5, 30, 6,
                                        List.of(Tag.GLUTEN_FREE, Tag.VEGETARIANA),
                                        List.of("1 plátano", "1/2 taza avena", "Canela",
                                                        "Chispas de chocolate (opcional)"),
                                        "1. Machaca plátano. 2. Mezcla con avena y canela. 3. Hornea 15 min a 180°C."),

                        new RecipeData("Yogur Griego con Miel y Nueces", "3 min", Difficulty.FÁCIL, MealCategory.SNACK,
                                        220, 16, 18, 10,
                                        List.of(Tag.RÁPIDA, Tag.ALTA_PROTEINA),
                                        List.of("Yogur griego", "1 cda miel", "1 puñado nueces"),
                                        "1. Sirve yogur. 2. Agrega miel y nueces."),

                        new RecipeData("Palomitas de Maíz con Especias", "10 min", Difficulty.FÁCIL, MealCategory.SNACK,
                                        150, 4, 28, 5,
                                        List.of(Tag.BAJO_EN_CALORIAS, Tag.ALTA_FIBRA),
                                        List.of("Maíz para palomitas", "Aceite de coco", "Pimentón, ajo en polvo, sal"),
                                        "1. Prepara palomitas. 2. Rocía con aceite y especias."),

                        new RecipeData("Rollitos de Jamón y Queso", "5 min", Difficulty.FÁCIL, MealCategory.SNACK, 190,
                                        14, 2, 14,
                                        List.of(Tag.RÁPIDA, Tag.ALTA_PROTEINA),
                                        List.of("2 rebanadas jamón de pavo", "Queso crema bajo grasa", "Espinacas"),
                                        "1. Unta queso en jamón. 2. Agrega espinacas. 3. Enrolla y corta en trozos."),

                        new RecipeData("Smoothie de Mango y Yogur", "6 min", Difficulty.FÁCIL, MealCategory.SNACK, 230,
                                        10, 42, 5,
                                        List.of(Tag.BAJO_EN_CALORIAS, Tag.VEGETARIANA),
                                        List.of("1/2 mango", "1/2 taza yogur", "1/4 taza leche", "Hielo"),
                                        "1. Licúa todos los ingredientes. 2. Sirve frío."),

                        new RecipeData("Tostadas de Hummus y Pepino", "7 min", Difficulty.FÁCIL, MealCategory.SNACK,
                                        200, 7, 28, 8,
                                        List.of(Tag.VEGETARIANA, Tag.RÁPIDA),
                                        List.of("2 tostadas integrales", "2 cda hummus", "Pepino en rodajas",
                                                        "Pimienta"),
                                        "1. Unta hummus en tostadas. 2. Coloca pepino. 3. Sazona con pimienta."),

                        new RecipeData("Barritas Energéticas Caseras", "20 min + refrigerar", Difficulty.MEDIO,
                                        MealCategory.SNACK, 240, 8, 32, 10,
                                        List.of(Tag.GLUTEN_FREE, Tag.ALTA_FIBRA),
                                        List.of("Dátiles", "Avena", "Nueces", "Semillas de chía", "Coco rallado"),
                                        "1. Licúa dátiles con avena. 2. Mezcla con nueces y chía. 3. Presiona en molde. 4. Refrigera 1h."));

        @PostConstruct
        public void init() {
                if (recipeRepository.count() == 0) {
                        logger.info("No hay recetas en la base de datos. Buscando usuario para asignar como creador...");

                        List<User> allUsers = userRepository.findAll();
                        if (allUsers.isEmpty()) {
                                logger.warn("No se encontraron usuarios en la base de datos. Saltando inicialización de recetas.");
                                return;
                        }

                        User adminUser = allUsers.get(0); // Usa el primer usuario
                        logger.info("Usuario encontrado: ID {}, Email: {}", adminUser.getId(), adminUser.getEmail());

                        List<Recipe> recipes = new ArrayList<>();
                        for (RecipeData data : RECIPE_DATA) {
                                Recipe recipe = new Recipe();
                                recipe.setTitle(data.title);
                                recipe.setTime(data.time);
                                recipe.setDifficulty(data.difficulty);
                                recipe.setCategory(data.category);
                                recipe.setCalories(data.calories);
                                recipe.setProtein(data.protein);
                                recipe.setCarbs(data.carbs);
                                recipe.setFat(data.fat);
                                recipe.setTags(new ArrayList<>(data.tags));
                                recipe.setIngredients(new ArrayList<>(data.ingredients));
                                recipe.setInstructions(data.instructions);
                                recipe.setIsPublic(true);
                                recipe.setCreatedBy(adminUser);
                                recipes.add(recipe);
                        }

                        recipeRepository.saveAll(recipes);
                        logger.info("✅ 50 recetas de ejemplo creadas exitosamente.");
                } else {
                        logger.info("Recetas ya existen. Inicialización omitida.");
                }
        }

        // Clase interna para estructurar los datos
        private static class RecipeData {
                final String title;
                final String time;
                final Difficulty difficulty;
                final MealCategory category;
                final Integer calories;
                final Integer protein;
                final Integer carbs;
                final Integer fat;
                final List<Tag> tags;
                final List<String> ingredients;
                final String instructions;

                RecipeData(String title, String time, Difficulty difficulty, MealCategory category,
                                Integer calories, Integer protein, Integer carbs, Integer fat,
                                List<Tag> tags, List<String> ingredients, String instructions) {
                        this.title = title;
                        this.time = time;
                        this.difficulty = difficulty;
                        this.category = category;
                        this.calories = calories;
                        this.protein = protein;
                        this.carbs = carbs;
                        this.fat = fat;
                        this.tags = tags;
                        this.ingredients = ingredients;
                        this.instructions = instructions;
                }
        }
}