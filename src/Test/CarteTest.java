// package Test;

// import elements.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;

// public class CarteTest {

//     private Carte carte;

//     @BeforeEach
//     public void setUp() {
//         carte = new Carte("TestMap");
//     }

//     @Test
//     public void testAjouterCentreEtCompteurs() {
//         carte.ajouterCentre("Hopital Central", 'O');
//         carte.ajouterCentre("Clinique Sud", 'M');
//         carte.ajouterCentre("Dispensaire Est", 'N');
//         carte.ajouterCentre("Dispensaire Ouest", 'N');

//         assertEquals(1, carte.getNbBloc(), "Le nombre de blocs opératoires devrait être 1");
//         assertEquals(1, carte.getNbMat(), "Le nombre de maternités devrait être 1");
//         assertEquals(2, carte.getNbNutri(), "Le nombre de centres de nutrition devrait être 2");
//         assertEquals(4, carte.getNbTotalCentres(), "Le nombre total de centres devrait être 4");
        
//         CentreSante centre = carte.getCentre(0);
//         assertNotNull(centre);
//         assertEquals("Hopital Central", centre.getNomCentre());
//         assertEquals('O', centre.getCenterType());
//     }

//     @Test
//     public void testAjouterRouteEtRecuperation() {
//         carte.ajouterCentre("Centre A", 'M');
//         carte.ajouterCentre("Centre B", 'O');
        
//         carte.ajouterRoute(0, 1, 8, 50, 60);

//         Route route = carte.getRoute(0, 1);
//         assertNotNull(route, "La route entre 0 et 1 devrait exister");
//         assertEquals(8, route.getFiabilite());
//         assertEquals(50, route.getDistance());
//         assertEquals(60, route.getDuree());
        
//         // La route doit être récupérable peu importe l'ordre des IDs passés en paramètre
//         Route routeInverse = carte.getRoute(1, 0);
//         assertNotNull(routeInverse, "La route entre 1 et 0 devrait exister (graphe non orienté)");
//         assertEquals(route, routeInverse);
//     }

//     @Test
//     public void testRouteCalculeDureeEstimee() {
//         carte.ajouterCentre("Centre A", 'M');
//         carte.ajouterCentre("Centre B", 'O');
        
//         // duree = 60, fiabilite = 8
//         carte.ajouterRoute(0, 1, 8, 50, 60);
//         Route route = carte.getRoute(0, 1);

//         // calcul attendu : 60 * (20 - 8) / 10 = 60 * 12 / 10 = 72
//         assertEquals(72, route.calculeDureeEstimee(), "La durée estimée calculée est incorrecte");
//     }

//     @Test
//     public void testConstruireGrapheAleatoire() {
//         Carte randomGraph = Carte.construireGrapheAleatoire("RandomMap", 10);
//         assertNotNull(randomGraph);
//         assertEquals("RandomMap", randomGraph.getId());
//         assertEquals(10, randomGraph.getNbTotalCentres(), "Le graphe devrait contenir 10 centres");
//     }
// }