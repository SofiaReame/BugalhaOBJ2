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

    
     //Representa uma entrada do pódio (nome e pontuação)
     
    public static class EntradaPodio {
        private String nome;
        private int pontuacao;

        public EntradaPodio(String nome, int pontuacao) {
           setNome(nome);
            setPontuacao(pontuacao);
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public int getPontuacao() {
            return pontuacao;
        }

        public void setPontuacao(int pontuacao) {
            this.pontuacao = pontuacao;
        }
    }


    //Adiciona uma nova pontuação ao pódio
    
    public void adicionarPontuacao(String nome, int pontuacao) {
        try {
            List<EntradaPodio> podio = lerPodio();
            podio.add(new EntradaPodio(nome, pontuacao));

            // Ordena da maior para a menor pontuação
            podio.sort((a, b) -> Integer.compare(b.getPontuacao(), a.getPontuacao()));

           

            salvarPodio(podio);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar o pódio: " + e.getMessage());
        }
    }

     //Lê o pódio a partir do arquivo XML
     
    public List<EntradaPodio> lerPodio() {
        List<EntradaPodio> podio = new ArrayList<>();
        try {
            File xmlFile = new File(ARQUIVO_XML);
            if (!xmlFile.exists()) {
                return podio; // Arquivo não existe, retorna lista vazia
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            NodeList nodes = doc.getElementsByTagName("entrada");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                String nome = element.getElementsByTagName("nome").item(0).getTextContent();
                int pontuacao = Integer.parseInt(element.getElementsByTagName("pontuacao").item(0).getTextContent());
                podio.add(new EntradaPodio(nome, pontuacao));
            }
        } catch (Exception e) {
            System.err.println("Erro ao ler o pódio: " + e.getMessage());
        }
        return podio;
    }

    // Salva o pódio no arquivo XML com formatação indentada
     
    private void salvarPodio(List<EntradaPodio> podio) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("podio");
            doc.appendChild(root);

            for (EntradaPodio entrada : podio) {
                Element e = doc.createElement("entrada");

                Element nome = doc.createElement("nome");
                nome.setTextContent(entrada.getNome());
                e.appendChild(nome);

                Element pontuacao = doc.createElement("pontuacao");
                pontuacao.setTextContent(String.valueOf(entrada.getPontuacao()));
                e.appendChild(pontuacao);

                root.appendChild(e);
            }

            salvarDocumento(doc);

        } catch (Exception e) {
            System.err.println("Erro ao salvar o pódio: " + e.getMessage());
        }
    }

    //Formata o XML de forma indentada e salva no disco

    private void salvarDocumento(Document doc) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            try (FileOutputStream out = new FileOutputStream(ARQUIVO_XML)) {
                transformer.transform(source, new StreamResult(out));
            }
        } catch (Exception e) {
            System.err.println("Erro ao salvar o arquivo XML: " + e.getMessage());
        }
    }

    
    //Exibe o pódio formatado
     
    public void exibirPodio() {
        List<EntradaPodio> podio = lerPodio();
        System.out.println("======= PÓDIO MAIORES PONTUAÇÕES =======");
        for (int i = 0; i < TAMANHO_MAXIMO; i++) {
            if (i < podio.size()) {
                EntradaPodio entrada = podio.get(i);
                System.out.printf("- %dº - %s : [%03d]%n", i + 1, entrada.getNome(), entrada.getPontuacao());
            } else {
                System.out.printf("- %dº - ==== : 000%n", i + 1);
            }
        }
    }
} 
