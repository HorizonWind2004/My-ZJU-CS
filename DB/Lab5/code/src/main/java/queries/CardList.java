package queries;

import entities.Card;

import java.util.List;

public class CardList {

    private int count;
    private List<Card> cards;

    public CardList(List<Card> cards) {
        this.count = cards.size();
        this.cards = cards;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Card> getCards() {
        return cards;
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < cards.size(); i++) {
            sb.append(cards.get(i).toJsonString());
            if (i != cards.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }


    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
