import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

public class CentreSante implements org.graphstream.graph.Node{
    int id ;
    char center_type ;

    @Override
    public Graph getGraph() {
        return null;
    }

    @Override
    public int getDegree() {
        return 0;
    }

    @Override
    public int getOutDegree() {
        return 0;
    }

    @Override
    public int getInDegree() {
        return 0;
    }

    @Override
    public boolean hasEdgeToward(String s) {
        return false;
    }

    @Override
    public boolean hasEdgeFrom(String s) {
        return false;
    }

    @Override
    public boolean hasEdgeBetween(String s) {
        return false;
    }

    @Override
    public <T extends Edge> T getEdgeToward(String s) {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeFrom(String s) {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeBetween(String s) {
        return null;
    }

    @Override
    public <T extends Edge> Iterator<T> getEdgeIterator() {
        return null;
    }

    @Override
    public <T extends Edge> Iterator<T> getEnteringEdgeIterator() {
        return null;
    }

    @Override
    public <T extends Edge> Iterator<T> getLeavingEdgeIterator() {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getNeighborNodeIterator() {
        return null;
    }

    @Override
    public <T extends Edge> T getEdge(int i) {
        return null;
    }

    @Override
    public <T extends Edge> T getEnteringEdge(int i) {
        return null;
    }

    @Override
    public <T extends Edge> T getLeavingEdge(int i) {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getBreadthFirstIterator() {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getBreadthFirstIterator(boolean b) {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getDepthFirstIterator() {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getDepthFirstIterator(boolean b) {
        return null;
    }

    @Override
    public <T extends Edge> Iterable<T> getEachEdge() {
        return null;
    }

    @Override
    public <T extends Edge> Iterable<T> getEachLeavingEdge() {
        return null;
    }

    @Override
    public <T extends Edge> Iterable<T> getEachEnteringEdge() {
        return null;
    }

    @Override
    public <T extends Edge> Collection<T> getEdgeSet() {
        return List.of();
    }

    @Override
    public <T extends Edge> Collection<T> getLeavingEdgeSet() {
        return List.of();
    }

    @Override
    public <T extends Edge> Collection<T> getEnteringEdgeSet() {
        return List.of();
    }

    @Override
    public boolean hasEdgeToward(Node node) {
        return false;
    }

    @Override
    public boolean hasEdgeToward(int i) throws IndexOutOfBoundsException {
        return false;
    }

    @Override
    public boolean hasEdgeFrom(Node node) {
        return false;
    }

    @Override
    public boolean hasEdgeFrom(int i) throws IndexOutOfBoundsException {
        return false;
    }

    @Override
    public boolean hasEdgeBetween(Node node) {
        return false;
    }

    @Override
    public boolean hasEdgeBetween(int i) throws IndexOutOfBoundsException {
        return false;
    }

    @Override
    public <T extends Edge> T getEdgeToward(Node node) {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeToward(int i) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeFrom(Node node) {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeFrom(int i) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeBetween(Node node) {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeBetween(int i) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public Iterator<Edge> iterator() {
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
