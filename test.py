from power_env import PowerSys
from RL_brain import DeepQNetwork
import matplotlib.pyplot as plt
import tensorflow as tf
import threading
import time
import sys
import socket
FLAG=True
tf.reset_default_graph()

env = PowerSys()
env.reset()
env.show()
RL = DeepQNetwork(n_actions=len(env.action_space),
                  n_features=len(env.observation),
                  learning_rate=0.01, e_greedy=0.9,
                  replace_target_iter=100, memory_size=2000,
                  e_greedy_increment=0.0006,)

loss = [env.state['loss']]
class trainThread (threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
        self.total_steps=0
    def run(self):
      if FLAG:
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect(('192.168.112.1',4700))
      for i_episode in range(50):
          observation = env.reset()
          total_reward = 0
          while True:
              if FLAG:
                szBuf = sock.recv(1024)
                szBuf = szBuf.decode('utf-8')
                if len(szBuf)>0 :
                  env.show()
              action = RL.choose_action(observation)

              observation_, reward, done = env.step(action)

              RL.store_transition(observation, action, reward, observation_)

              total_reward += reward

              if self.total_steps > 600:
                  RL.learn()
                  if RL.learn_step_counter % RL.replace_target_iter==0:
                    loss.append(env.state['loss'])
                    print("Loss: ", env.state['loss'])
                    if not FLAG:
                      env.show()
                    
              if done:
                  print('episode: ', i_episode,
                        'reward: ', round(total_reward, 2),
                        'epsilon:',round(RL.epsilon,2),
                        ' loss: ', env.state['loss'])
                  break

              observation = observation_
              self.total_steps += 1

      # plot loss and cost
      fig = plt.figure()
      plt.plot(loss)
      fig.savefig('learncurve.png', dpi=300)
      plt.show()

        

thread1 = trainThread()
thread1.start()