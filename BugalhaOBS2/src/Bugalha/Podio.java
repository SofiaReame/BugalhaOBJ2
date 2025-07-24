package Bugalha;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


// O arquivo XML será salvo como "podio.xml" na raiz do projeto
 
public class Podio {
    private static final String ARQUIVO_XML = "podio.xml";
    private List<EntradaPodio> entradas;

    public Podio() {
        entradas = new ArrayList<>();
    }

    
    //Carrega o arquivo de pódio. Se não existir, cria a estrutura básica
    //Em caso de erro, imprime mensagem e mantém lista vazia
     
    public void carregarPodio() {
        try {
            File arquivo = new File(ARQUIVO_XML);
            if (!arquivo.exists()) {
                Document doc = criarDocumento();
                Element raiz = doc.createElement("podio");
                doc.appendChild(raiz);
                salvarDocumento(doc);
            }

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(ARQUIVO_XML));
            NodeList listaEntradas = doc.getElementsByTagName("entrada");
            entradas.clear();

            for (int i = 0; i < listaEntradas.getLength(); i++) {
                Element elem = (Element) listaEntradas.item(i);
                String nome = elem.getElementsByTagName("nome").item(0).getTextContent();
                int pontos = Integer.parseInt(
                    elem.getElementsByTagName("pontos").item(0).getTextContent()
                );
                entradas.add(new EntradaPodio(nome, pontos));
            }

            // Ordena por pontuação decrescente e mantém top 5
            Collections.sort(
                entradas,
                Comparator.comparingInt(EntradaPodio::getPontos).reversed()
            );
            if (entradas.size() > 5) {
                entradas = entradas.subList(0, 5);
            }
        } catch (Exception ex) {
            System.err.println("Erro ao carregar o pódio: " + ex.getMessage());
            entradas.clear();
        }
    }

    
    //Salva uma nova pontuação no pódio e atualiza o arquivo XML
    //Em caso de erro imprime
    
    public void salvarPontuacao(String nomeJogador, int pontos) {
        try {
            carregarPodio();
            entradas.add(new EntradaPodio(nomeJogador, pontos));

            Collections.sort(
                entradas,
                Comparator.comparingInt(EntradaPodio::getPontos).reversed()
            );
            if (entradas.size() > 5) {
                entradas = entradas.subList(0, 5);
            }

            Document doc = criarDocumento();
            Element raiz = doc.createElement("podio");
            doc.appendChild(raiz);
            for (EntradaPodio e : entradas) {
                Element entry = doc.createElement("entrada");
                Element nomeElem = doc.createElement("nome");
                nomeElem.appendChild(doc.createTextNode(e.getNome()));
                Element pontosElem = doc.createElement("pontos");
                pontosElem.appendChild(doc.createTextNode(String.valueOf(e.getPontos())));
                entry.appendChild(nomeElem);
                entry.appendChild(pontosElem);
                raiz.appendChild(entry);
            }
            salvarDocumento(doc);
        } catch (Exception ex) {
            System.err.println("Erro ao atualizar o pódio: " + ex.getMessage());
        }
    }

    
    //Retorna as entradas carregadas do pódio
    
    public List<EntradaPodio> getEntradas() {
        return entradas;
    }


    /**
     * Exibe o pódio formatado no console.
     */
    public void exibirPodio() {
        carregarPodio();
        System.out.println("======= PÓDIO MAIORES PONTUAÇÕES =======\n");
        for (int i = 0; i < 5; i++) {
            if (i < entradas.size()) {
                EntradaPodio e = entradas.get(i);
                System.out.printf("- %dº - %s : [%03d]%n", i + 1, e.getNome(), e.getPontos());
            } else {
                System.out.printf("- %dº - ==== : [000]%n", i + 1);
            }
        }
    }

    // Cria um Document XML em branco
    private Document criarDocumento() throws ParserConfigurationException {
        return DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
            .newDocument();
    }

    // Grava o Document em formato indentado no disco
    private void salvarDocumento(Document doc)
            throws TransformerException, IOException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(
            "{http://xml.apache.org/xslt}indent-amount", "2"
        );
        DOMSource source = new DOMSource(doc);
        try (FileOutputStream out = new FileOutputStream(ARQUIVO_XML)) {
            transformer.transform(source, new StreamResult(out));
        }
    }

    
    //Representa uma entrada do pódio: nome e pontos
    
    public static class EntradaPodio {
        private final String nome;
        private final int pontos;
        public EntradaPodio(String nome, int pontos) {
            this.nome = nome;
            this.pontos = pontos;
        }
        public String getNome() { return nome; }
        public int getPontos() { return pontos; }
    }
}