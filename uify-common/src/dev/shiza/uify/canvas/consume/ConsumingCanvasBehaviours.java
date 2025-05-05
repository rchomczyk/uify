package dev.shiza.uify.canvas.consume;

public interface ConsumingCanvasBehaviours {

    ConsumingCanvas onConsumption(final ConsumptionBehaviour<ConsumptionResult> consumptionBehaviour);

    ConsumingCanvas onIndexedConsumption(final ConsumptionBehaviour<IndexedConsumptionResult> consumptionBehaviour);
}
