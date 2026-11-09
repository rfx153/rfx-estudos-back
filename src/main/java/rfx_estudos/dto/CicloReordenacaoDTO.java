package rfx_estudos.dto;

import java.util.ArrayList;
import java.util.List;

public class CicloReordenacaoDTO {

    private List<Item> itens = new ArrayList<>();

    public CicloReordenacaoDTO() {}

    public List<Item> getItens() { return itens; }
    public void setItens(List<Item> itens) { this.itens = itens; }

    public static class Item {

        private Long id;
        private Integer ordem;

        public Item() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Integer getOrdem() { return ordem; }
        public void setOrdem(Integer ordem) { this.ordem = ordem; }
    }
}
