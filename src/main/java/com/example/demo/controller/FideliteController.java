@RestController
@RequestMapping("/api/client/fidelite")
public class FideliteController {

    private final FideliteService fideliteService;
    private final ClientRepository clientRepository;

    public FideliteController(FideliteService fideliteService, ClientRepository clientRepository) {
        this.fideliteService = fideliteService;
        this.clientRepository = clientRepository;
    }

    // === Client ===

    @GetMapping("/{clientId}")
    public ResponseEntity<Fidelite> getFidelite(@PathVariable Long clientId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) return ResponseEntity.notFound().build();

        Optional<Fidelite> fidelite = fideliteService.getFideliteByClient(clientOpt.get());
        return fidelite.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.ok(new Fidelite(clientOpt.get())));
    }

    @PostMapping("/{clientId}/ajouter")
    public ResponseEntity<Fidelite> ajouterPoints(@PathVariable Long clientId, @RequestParam int points) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) return ResponseEntity.notFound().build();

        Fidelite fidelite = fideliteService.ajouterPoints(clientOpt.get(), points);
        return ResponseEntity.ok(fidelite);
    }

    @PostMapping("/{clientId}/utiliser")
    public ResponseEntity<?> utiliserPoints(@PathVariable Long clientId, @RequestParam int points) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) return ResponseEntity.notFound().build();

        try {
            Fidelite fidelite = fideliteService.utiliserPoints(clientOpt.get(), points);
            return ResponseEntity.ok(fidelite);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // === Gestionnaire (optionnel) ===
    // @GetMapping("/all") 
    // public List<Fidelite> getAllFidelites() {
    //     return fideliteService.getAllFidelites();
    // }
}
