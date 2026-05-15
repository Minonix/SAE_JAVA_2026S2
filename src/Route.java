import org.graphstream.graph.Node;

import java.util.*;

public class Route implements org.graphstream.graph.Edge {
    int distance, fiabilite, duree ;

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public boolean isLoop() {
        return false;
    }

    @Override
    public <T extends Node> T getNode0() {
        return null;
    }

    @Override
    public <T extends Node> T getNode1() {
        return null;
    }

    @Override
    public <T extends Node> T getSourceNode() {
        return null;
    }

    @Override
    public <T extends Node> T getTargetNode() {
        return null;
    }

    @Override
    public <T extends Node> T getOpposite(Node node) {
        return null;
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public <T> T getAttribute(String s) {
        return null;
    }

    @Override
    public <T> T getFirstAttributeOf(String... strings) {
        return null;
    }

    @Override
    public <T> T getAttribute(String s, Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T getFirstAttributeOf(Class<T> aClass, String... strings) {
        return null;
    }

    @Override
    public CharSequence getLabel(String s) {
        return null;
    }

    @Override
    public double getNumber(String s) {
        return 0;
    }

    @Override
    public ArrayList<? extends Number> getVector(String s) {
        return null;
    }

    @Override
    public Object[] getArray(String s) {
        return new Object[0];
    }

    @Override
    public HashMap<?, ?> getHash(String s) {
        return null;
    }

    @Override
    public boolean hasAttribute(String s) {
        return false;
    }

    @Override
    public boolean hasAttribute(String s, Class<?> aClass) {
        return false;
    }

    @Override
    public boolean hasLabel(String s) {
        return false;
    }

    @Override
    public boolean hasNumber(String s) {
        return false;
    }

    @Override
    public boolean hasVector(String s) {
        return false;
    }

    @Override
    public boolean hasArray(String s) {
        return false;
    }

    @Override
    public boolean hasHash(String s) {
        return false;
    }

    @Override
    public Iterator<String> getAttributeKeyIterator() {
        return null;
    }

    @Override
    public Iterable<String> getEachAttributeKey() {
        return null;
    }

    @Override
    public Collection<String> getAttributeKeySet() {
        return List.of();
    }

    @Override
    public void clearAttributes() {

    }

    @Override
    public void addAttribute(String s, Object... objects) {

    }

    @Override
    public void changeAttribute(String s, Object... objects) {

    }

    @Override
    public void setAttribute(String s, Object... objects) {

    }

    @Override
    public void addAttributes(Map<String, Object> map) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public int getAttributeCount() {
        return 0;
    }
}
